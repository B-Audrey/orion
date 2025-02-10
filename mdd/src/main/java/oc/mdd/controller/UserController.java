package oc.mdd.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oc.mdd.dto.UserSigninDto;
import oc.mdd.entity.UserEntity;
import oc.mdd.model.ErrorResponseModel;
import oc.mdd.service.UserService;
import oc.mdd.utils.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserService userService;
    private final Utils strongPasswordValidator;

    @PostMapping("new")
    public ResponseEntity<?> signin(@RequestBody UserSigninDto userSigninDto) {
        try {
            boolean isPasswordStrong = strongPasswordValidator.isPasswordValid(userSigninDto.getPassword());
            if (!isPasswordStrong) {
                String message = "Password is not strong enough";
                log.warn(message);
                ErrorResponseModel errorResponse = new ErrorResponseModel(HttpStatus.UNAUTHORIZED, message);
                return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
            }
            UserEntity newUser = userService.registerUser(userSigninDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (Exception e) {
            String message = e.getMessage();
            log.warn(message);
            ErrorResponseModel errorResponse = new ErrorResponseModel(HttpStatus.UNAUTHORIZED, message);
            return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
        }
    }

}
