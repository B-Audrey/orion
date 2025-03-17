package oc.mdd.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oc.mdd.dto.PostCreationDto;
import oc.mdd.entity.PostEntity;
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

    @PostMapping("new")
    public ResponseEntity<?> createPost(@RequestBody PostCreationDto post) {
        try {
            PostEntity newPost = postService.createPost(post);
            return ResponseEntity.status(HttpStatus.CREATED).body(newPost);
        } catch (Exception e) {
            String message = e.getMessage();
            log.warn(message);
            throw new BadRequestException(message);
        }
    }
}
