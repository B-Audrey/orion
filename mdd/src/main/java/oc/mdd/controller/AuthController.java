package oc.mdd.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oc.mdd.config.JwtUtils;
import oc.mdd.dto.AuthLoginDto;
import oc.mdd.entity.UserEntity;
import oc.mdd.model.ErrorResponseModel;
import oc.mdd.model.UserModel;
import oc.mdd.service.UserService;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/me")
    public ResponseEntity<?> returnMeInfos(HttpServletRequest request) {
        try {
            UserEntity user = (UserEntity) request.getAttribute("user");
            if (user != null) {
                UserModel me = userService.convertToUserModel(user);
                return ResponseEntity.ok(me);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        } catch (Exception e) {
            if ("user not found".equals(e.getMessage())) {
                log.error("An unknown user tried to ask for me info with a valid token !!! ");
                ErrorResponseModel errorResponse = new ErrorResponseModel(HttpStatus.NOT_FOUND, e.getMessage());
                return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
            }
            log.error(e.getMessage());
            ErrorResponseModel errorResponse = new ErrorResponseModel(HttpStatus.UNAUTHORIZED, e.getMessage());
            return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
        }
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refreshToken(@CookieValue(value = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {
        try {
            if (refreshToken == null) {
                ErrorResponseModel errorResponse = new ErrorResponseModel(HttpStatus.UNAUTHORIZED, "error");
                return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
            }
            String email = jwtUtils.extractUserEmail(refreshToken);
            UserEntity user = userService.getUserByEmail(email);
            if (user == null) {
                ErrorResponseModel errorResponse = new ErrorResponseModel(HttpStatus.UNAUTHORIZED, "error");
                return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
            }
            String accessToken = jwtUtils.generateToken(email);
            String newRefreshToken = jwtUtils.generateRefreshToken(email);
            Cookie refreshTokenCookie = new Cookie("refreshToken", newRefreshToken);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setPath("/api/auth/refresh");
            refreshTokenCookie.setMaxAge(86400000); // modifier ici avec la variable
            response.addCookie(refreshTokenCookie);

            Map<String, Object> authData = new HashMap<>();
            authData.put("token", accessToken);
            return ResponseEntity.ok(authData);
        } catch (Exception e) {
            String message = e.getMessage();
            log.warn(message);
            ErrorResponseModel errorResponse = new ErrorResponseModel(HttpStatus.UNAUTHORIZED, "error");
            return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
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
            if (authentication.isAuthenticated()) {
                String accessToken = jwtUtils.generateToken(authLoginDto.getUsername());
                String refreshToken = jwtUtils.generateRefreshToken(authLoginDto.getUsername());
                Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
                refreshTokenCookie.setHttpOnly(true);
                refreshTokenCookie.setPath("/api/auth/refresh");
                refreshTokenCookie.setMaxAge(86400000);
                response.addCookie(refreshTokenCookie);

                Map<String, Object> authData = new HashMap<>();
                authData.put("token", accessToken);
                return ResponseEntity.ok(authData);
            }
            ErrorResponseModel errorResponse = new ErrorResponseModel(HttpStatus.UNAUTHORIZED, "error");
            return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
        } catch (Exception e) {
            String message = e.getMessage();
            log.warn(message);
            ErrorResponseModel errorResponse = new ErrorResponseModel(HttpStatus.UNAUTHORIZED, "error");
            return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/api/auth/refresh");
        refreshTokenCookie.setMaxAge(0);
        response.addCookie(refreshTokenCookie);
        return ResponseEntity.ok("Logout successful");
    }



}
