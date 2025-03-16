package oc.mdd.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oc.mdd.dto.PaginationQueryDto;
import oc.mdd.dto.PostCreationDto;
import oc.mdd.entity.PostEntity;
import oc.mdd.entity.UserEntity;
import oc.mdd.model.PageModel;
import oc.mdd.model.error.BadRequestException;
import oc.mdd.service.PostService;
import org.springframework.data.domain.Page;
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

    @GetMapping("my-feed")
    public ResponseEntity<?> getAllPosts(
            HttpServletRequest request,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sort)
            {
        try {
            UserEntity user = (UserEntity) request.getAttribute("user");
            PaginationQueryDto pageDto = new PaginationQueryDto(page, size, sort);
            Page<PostEntity> topics = this.postService.getUserFeed(user.getUuid(), pageDto);

            PageModel<PostEntity> topicsPage = new PageModel<PostEntity>(
                    topics.getContent(),
                    new PageModel.Pagination(
                            topics.getTotalElements(),
                            topics.getNumber(),
                            topics.getSize()
                    )
            );

            return ResponseEntity.ok(topicsPage);
        } catch (Exception e) {
            String message = e.getMessage();
            log.warn(message);
            throw new BadRequestException(message);
        }
    }

    @GetMapping("{uuid}")
    public ResponseEntity<?> getPost(
            @PathVariable String uuid
            ) {
        try {
            PostEntity post = postService.findByUuid(uuid);
            return ResponseEntity.ok(post);
        } catch (Exception e) {
            String message = e.getMessage();
            log.warn(message);
            throw new BadRequestException(message);
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
