package oc.mdd.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oc.mdd.dto.CommentCreationDto;
import oc.mdd.dto.PostCreationDto;
import oc.mdd.entity.PostEntity;
import oc.mdd.entity.UserEntity;
import oc.mdd.model.PostModel;
import oc.mdd.model.error.BadRequestException;
import oc.mdd.model.error.NotFoundException;
import oc.mdd.service.PostService;
import oc.mdd.utils.mappers.PostMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;

    /**
     * get post by uuid
     *
     * @param uuid the uuid of the post
     * @return one full post with its linked comments
     */
    @GetMapping("{uuid}")
    public ResponseEntity<?> getPost(
            @PathVariable String uuid
    ) {
        try {
            PostEntity post = postService.findByUuidWithComments(uuid);
            PostModel postModel = postMapper.convertToPostModel(post);
            return ResponseEntity.ok(postModel);
        } catch (Exception e) {
            String message = e.getMessage();
            log.warn(message);
            throw new NotFoundException(message);
        }
    }

    /**
     * post a comment on a post
     *
     * @param request            the request will contain the user extracted from the token by jwt filter
     * @param uuid               the uuid of the post who will receive the comment
     * @param commentCreationDto the content of the comment
     * @return the full post including the new comment
     */
    @PostMapping("{uuid}/comment")
    public ResponseEntity<?> postComment(
            HttpServletRequest request,
            @PathVariable String uuid,
            @RequestBody CommentCreationDto commentCreationDto
    ) {
        try {
            UserEntity user = (UserEntity) request.getAttribute("user");
            PostEntity savedPost = this.postService.postCommentOnPost(uuid, commentCreationDto.getContent(), user);
            PostModel postModel = postMapper.convertToPostModel(savedPost);
            return ResponseEntity.status(HttpStatus.CREATED).body(postModel);
        } catch (Exception e) {
            String message = e.getMessage();
            log.warn(message);
            throw new NotFoundException(message);
        }
    }


    /**
     * create a new post
     *
     * @param request the request will contain the user extracted from the token by jwt filter
     * @param post    the post to create
     * @return the created post
     */
    @PostMapping("new")
    public ResponseEntity<?> createPost(HttpServletRequest request, @RequestBody PostCreationDto post) {
        try {
            UserEntity user = (UserEntity) request.getAttribute("user");
            PostEntity newPost = postService.createPost(post, user);
            PostModel postModel = postMapper.convertToPostModel(newPost);
            return ResponseEntity.status(HttpStatus.CREATED).body(postModel);
        } catch (Exception e) {
            String message = e.getMessage();
            log.warn(message);
            throw new BadRequestException(message);
        }
    }
}
