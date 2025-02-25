package oc.mdd.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oc.mdd.dto.UserSigninDto;
import oc.mdd.dto.UserUpdateDto;
import oc.mdd.entity.UserEntity;
import oc.mdd.model.UserModel;
import oc.mdd.model.error.BadRequestException;
import oc.mdd.model.error.UnauthorizedException;
import oc.mdd.service.UserService;
import oc.mdd.utils.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
                throw new BadRequestException(message);
            }
            UserEntity newUser = userService.registerUser(userSigninDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (Exception e) {
            String message = e.getMessage();
            log.warn(message);
            throw new UnauthorizedException(message);
        }
    }

    @PutMapping("{uuid}")
    public ResponseEntity<?> update(HttpServletRequest request, @PathVariable String uuid, @RequestBody UserUpdateDto userUpdateDto) {
        try {
            UserEntity user = (UserEntity) request.getAttribute("user");
            if (uuid == null || !uuid.equals(user.getUuid())) {
                String message = "You can only update your own account";
                throw new UnauthorizedException(message);
            }
            UserEntity updatedUser = userService.updateUser(uuid, userUpdateDto);
            UserModel userModel = userService.convertToUserModel(updatedUser);
            return ResponseEntity.ok(userModel);
        } catch (Exception e) {
            String message = e.getMessage();
            log.warn(message);
            throw new UnauthorizedException(message);
        }
    }

}
