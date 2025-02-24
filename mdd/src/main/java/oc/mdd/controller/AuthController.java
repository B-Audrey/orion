package oc.mdd.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oc.mdd.config.JwtUtils;
import oc.mdd.dto.AuthLoginDto;
import oc.mdd.entity.UserEntity;
import oc.mdd.model.*;
import oc.mdd.model.error.ForbiddenException;
import oc.mdd.model.error.NotFoundException;
import oc.mdd.model.error.UnauthorizedException;
import oc.mdd.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AuthController {
    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Value("${app.refresh-expiration-time}")
    private String refreshExpirationTime;

    @GetMapping("/me")
    public ResponseEntity<?> returnMeInfos(HttpServletRequest request) {
        try {
            UserEntity user = (UserEntity) request.getAttribute("user");
            if (user != null) {
                UserModel me = userService.convertToUserModel(user);
                return ResponseEntity.ok(me);
            }
            throw new UnauthorizedException("user not found");
        } catch (Exception e) {
            if ("user not found".equals(e.getMessage())) {
                log.error("An unknown user tried to ask for me info with a valid token !!! ");
                throw new NotFoundException(e.getMessage());
            }
            log.error(e.getMessage());
            throw new ForbiddenException(e.getMessage());
        }
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refreshToken(@CookieValue(value = "mddRefreshToken", required = false) String refreshToken, HttpServletResponse response) {
        try {
            if (refreshToken == null) {
                throw new UnauthorizedException("error");
            }
            String email = jwtUtils.extractUserEmail(refreshToken);
            UserEntity user = userService.getUserByEmail(email);
            if (user == null) {
                throw new UnauthorizedException("error");
            }
            String accessToken = jwtUtils.generateToken(email);
            String newRefreshToken = jwtUtils.generateRefreshToken(email);
            Cookie refreshTokenCookie = new Cookie("mddRefreshToken", newRefreshToken);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setPath("/api/auth/refresh");
            refreshTokenCookie.setMaxAge(Integer.parseInt(refreshExpirationTime));
            response.addCookie(refreshTokenCookie);

            Map<String, Object> authData = new HashMap<>();
            authData.put("token", accessToken);
            return ResponseEntity.ok(authData);
        } catch (Exception e) {
            String message = e.getMessage();
            log.warn(message);
            throw new UnauthorizedException(message);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthLoginDto authLoginDto, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authLoginDto.getUsername(),
                            authLoginDto.getPassword()
                    )
            );
            UserEntity user = userService.findUserByNameOrMail(authLoginDto.getUsername());
            if (user == null) {
                throw new UnauthorizedException("error");
            }
            if (authentication.isAuthenticated()) {
                String accessToken = jwtUtils.generateToken(user.getEmail());
                String refreshToken = jwtUtils.generateRefreshToken(user.getEmail());
                Cookie refreshTokenCookie = new Cookie("mddRefreshToken", refreshToken);
                refreshTokenCookie.setHttpOnly(true);
                refreshTokenCookie.setPath("/api/auth/refresh");
                refreshTokenCookie.setMaxAge(Integer.parseInt(refreshExpirationTime));
                response.addCookie(refreshTokenCookie);

                Map<String, Object> authData = new HashMap<>();
                authData.put("token", accessToken);
                return ResponseEntity.ok(authData);
            }
            throw new UnauthorizedException("error");

        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new UnauthorizedException("error");
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie("mddRefreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/api/auth/refresh");
        refreshTokenCookie.setMaxAge(0);
        response.addCookie(refreshTokenCookie);
        Map<String, Object> message = new HashMap<>();
        message.put("message", "ok");
        return ResponseEntity.ok(message);
    }

}
