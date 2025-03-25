package oc.mdd.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oc.mdd.dto.PaginationQueryDto;
import oc.mdd.dto.UserPasswordDto;
import oc.mdd.dto.UserSigninDto;
import oc.mdd.dto.UserUpdateDto;
import oc.mdd.entity.PostEntity;
import oc.mdd.entity.UserEntity;
import oc.mdd.model.PageModel;
import oc.mdd.model.PostListModel;
import oc.mdd.model.UserModel;
import oc.mdd.model.error.BadRequestException;
import oc.mdd.model.error.ForbiddenException;
import oc.mdd.model.error.UnauthorizedException;
import oc.mdd.service.PostService;
import oc.mdd.service.UserService;
import oc.mdd.utils.JwtUtils;
import oc.mdd.utils.PasswordUtil;
import oc.mdd.utils.mappers.PostMapper;
import oc.mdd.utils.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserService userService;
    private final PostService postService;
    private final UserMapper userMapper;
    private final PostMapper postMapper;
    private final PasswordUtil strongPasswordValidator;
    private final JwtUtils jwtUtils;

    @Value("${app.refresh-expiration-time}")
    private String refreshExpirationTime;

    /**
     * a user make subscription to a topic
     *
     * @param request   the request will contain the user extracted from the token by jwt filter
     * @param userUuid  the uuid of the user to get
     * @param topicUuid the uuid of the topic to add
     * @return the user updated with its subscriptions including the new one
     */
    @GetMapping("{userUuid}/topic-subscription/{topicUuid}")
    public ResponseEntity<?> addTopic(HttpServletRequest request, @PathVariable String userUuid,
            @PathVariable String topicUuid) {
        try {
            UserEntity user = (UserEntity) request.getAttribute("user");
            if (userUuid == null || !userUuid.equals(user.getUuid())) {
                String message = "You can only update your own account";
                throw new UnauthorizedException(message);
            }
            UserEntity updatedUser = userService.addTopic(userUuid, topicUuid);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            String message = e.getMessage();
            log.warn(message);
            throw new UnauthorizedException(message);
        }

    }

    /**
     * a user unsubscribe from a topic
     *
     * @param request   the request will contain the user extracted from the token by jwt filter
     * @param userUuid  the uuid of the user to get
     * @param topicUuid the uuid of the topic to remove
     * @return the user updated with its subscriptions without the removed one
     */
    @GetMapping("{userUuid}/topic-unsubscription/{topicUuid}")
    public ResponseEntity<?> removeTopic(HttpServletRequest request, @PathVariable String userUuid,
            @PathVariable String topicUuid) {
        try {
            UserEntity user = (UserEntity) request.getAttribute("user");
            if (user == null) {
                String message = "You can only update your own account";
                throw new UnauthorizedException(message);
            }
            UserEntity updatedUser = userService.removeTopic(userUuid, topicUuid);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            String message = e.getMessage();
            log.warn(message);
            throw new UnauthorizedException(message);
        }
    }

    /**
     * get the feed of the user, it will return posts where topics are includes in the user ones
     *
     * @param request the request will contain the user extracted from the token by jwt filter
     * @param page    the page number
     * @param size    the size of the page
     * @param sort    the sort of the page
     * @return the feed of the user
     */
    @GetMapping("my-feed")
    public ResponseEntity<?> getUserFeed(
            HttpServletRequest request,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sort) {
        try {
            UserEntity user = (UserEntity) request.getAttribute("user");
            PaginationQueryDto pageDto = new PaginationQueryDto(page, size, sort);
            Page<PostEntity> postEntityPage = this.postService.getUserFeed(user.getUuid(), pageDto);
            List<PostEntity> postModelContent = postEntityPage.getContent();
            List<PostListModel> postListModels = postModelContent.stream()
                    .map(postMapper::convertToPostListModel)
                    .toList();
            PageModel<PostListModel> postModelPage = new PageModel<PostListModel>(
                    postListModels,
                    new PageModel.Pagination(
                            postEntityPage.getTotalElements(),
                            postEntityPage.getNumber(),
                            postEntityPage.getSize()
                    )
            );
            return ResponseEntity.ok(postModelPage);
        } catch (Exception e) {
            String message = e.getMessage();
            log.warn(message);
            throw new BadRequestException(message);
        }
    }

    /**
     * add a new user
     *
     * @param userSigninDto the user to add
     * @return the new created user
     */
    @PostMapping("/new")
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

    /**
     * update the user
     *
     * @param request       the request will contain the user extracted from the token by jwt filter
     * @param uuid          the uuid of the user to update
     * @param userUpdateDto the new user infos
     * @param response      the response to add the new refresh token in cookies including new data
     * @return the updated user with its cookie in response
     */
    @PutMapping("{uuid}")
    public ResponseEntity<?> updateUser(HttpServletRequest request, @PathVariable String
            uuid, @RequestBody UserUpdateDto userUpdateDto, HttpServletResponse response) {
        try {
            UserEntity user = (UserEntity) request.getAttribute("user");
            if (uuid == null || !uuid.equals(user.getUuid())) {
                String message = "You can only update your own account";
                throw new ForbiddenException(message);
            }
            UserEntity updatedUser = userService.updateUser(uuid, userUpdateDto);
            String refreshToken = jwtUtils.generateRefreshToken(updatedUser.getEmail()); //nedd to refresh jwt with
            // new mail
            Cookie refreshTokenCookie = new Cookie("mddRefreshToken", refreshToken);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setPath("/api/auth/refresh");
            refreshTokenCookie.setMaxAge(Integer.parseInt(refreshExpirationTime));
            response.addCookie(refreshTokenCookie);
            UserModel userModel = userMapper.convertToUserModel(updatedUser);
            return ResponseEntity.ok(userModel);
        } catch (Exception e) {
            String message = e.getMessage();
            log.warn(message);
            throw new ForbiddenException(message);
        }
    }

    /**
     * update the password of the user
     * use patch because it is a partial update
     *
     * @param request         the request will contain the user extracted from the token by jwt filter
     * @param uuid            the uuid of the user to update
     * @param userPasswordDto the ancient and the new password
     * @return an "ok" message to confirm the update
     */
    @PatchMapping("{uuid}/password")
    public ResponseEntity<?> updatePassword(HttpServletRequest request, @PathVariable String
            uuid, @RequestBody UserPasswordDto userPasswordDto) {
        try {
            UserEntity user = (UserEntity) request.getAttribute("user");
            if (uuid == null || !uuid.equals(user.getUuid())) {
                throw new ForbiddenException("You can only update your own password");
            }
            boolean isPasswordStrong = strongPasswordValidator.isPasswordValid(userPasswordDto.getNewPassword());
            if (!isPasswordStrong) {
                String message = "Password is not strong enough";
                throw new BadRequestException(message);
            }
            userService.updatePassword(uuid, userPasswordDto);
            Map<String, Object> message = new HashMap<>();
            message.put("message", "ok");
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            String message = e.getMessage();
            log.warn(message);
            throw new ForbiddenException(message);
        }
    }

}
