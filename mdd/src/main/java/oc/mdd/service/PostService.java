package oc.mdd.service;

import lombok.RequiredArgsConstructor;
import oc.mdd.dto.PaginationQueryDto;
import oc.mdd.dto.PostCreationDto;
import oc.mdd.entity.PostEntity;
import oc.mdd.entity.TopicEntity;
import oc.mdd.model.PostModel;
import oc.mdd.model.TopicModel;
import oc.mdd.model.UserModel;
import oc.mdd.repository.PostRepository;
import oc.mdd.utils.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final TopicService topicService;
    private final UserService userService;

    public PostModel convertToModel(PostEntity postEntity) {
        return new PostModel(
                postEntity.getUuid(),
                postEntity.getTitle(),
                postEntity.getContent(),
                this.topicService.convertToTopicModel(postEntity.getTopic()),
                this.userService.convertToUserModel(postEntity.getUser()),
                postEntity.getCreatedAt(),
                postEntity.getUpdatedAt(),
                postEntity.getDeletedAt()

        );
    }

    public PostEntity findByUuid(String uuid) {
        return this.postRepository.findByUuid(uuid);
    }

    public PostEntity createPost(PostCreationDto post) {
        TopicEntity topicEntity = this.topicService.getTopicByUuid(post.getTopicUuid());
        PostEntity postEntity = new PostEntity();
        postEntity.setTitle(post.getTitle());
        postEntity.setContent(post.getContent());
        postEntity.setTopic(topicEntity);
        return this.postRepository.save(postEntity);
    }

    public Page<PostEntity> getUserFeed(String uuid, PaginationQueryDto pageDto) {
        Pageable pageable = PaginationUtil.createPageable(pageDto.getPage(), pageDto.getSize(), pageDto.getSort());
        return postRepository.findUserFeed(uuid, pageable);

    }
}

