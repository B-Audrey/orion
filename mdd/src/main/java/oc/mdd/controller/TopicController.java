package oc.mdd.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oc.mdd.dto.PaginationQueryDto;
import oc.mdd.entity.TopicEntity;
import oc.mdd.model.PageModel;
import oc.mdd.model.error.BadRequestException;
import oc.mdd.service.TopicService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
@Slf4j
@Validated
public class TopicController {

    private final TopicService topicService;

    @GetMapping
    public ResponseEntity<?> getAllTopics(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String search) {
        try {
            PaginationQueryDto pageDto = new PaginationQueryDto(page, size, sort);
            String searchString = search == null ? "" : search;
            Page<TopicEntity> topics = this.topicService.getTopics(pageDto, searchString);
            PageModel<TopicEntity> topicsPage = new PageModel<>(
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
}
