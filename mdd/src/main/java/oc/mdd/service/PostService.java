package oc.mdd.service;

import lombok.RequiredArgsConstructor;
import oc.mdd.dto.PaginationQueryDto;
import oc.mdd.dto.PostCreationDto;
import oc.mdd.entity.PostEntity;
import oc.mdd.entity.TopicEntity;
import oc.mdd.repository.PostRepository;
import oc.mdd.utils.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final TopicService topicService;


    public PostEntity findByUuidWithComments(String uuid) {
        return this.postRepository.findByUuidWithComments(uuid);
    }

    public PostEntity createPost(PostCreationDto post) {
        TopicEntity topicEntity = this.topicService.getTopicByUuid(post.getTopicUuid());
        PostEntity postEntity = new PostEntity();
        postEntity.setTitle(post.getTitle());
        postEntity.setContent(post.getContent());
        postEntity.setTopic(topicEntity);
        return this.postRepository.save(postEntity);
    }

    @Transactional
    public Page<PostEntity> getUserFeed(String uuid, PaginationQueryDto pageDto) {
        Pageable pageable = PaginationUtil.createPageable(pageDto.getPage(), pageDto.getSize(), pageDto.getSort());
        return postRepository.findUserFeed(uuid, pageable);

    }
}

