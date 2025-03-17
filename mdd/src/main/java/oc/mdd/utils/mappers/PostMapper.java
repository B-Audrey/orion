package oc.mdd.utils.mappers;

import lombok.RequiredArgsConstructor;
import oc.mdd.entity.PostEntity;
import oc.mdd.model.PostListModel;
import oc.mdd.model.PostModel;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostMapper {

    private final PartialUserMapper userMapper;
    private final TopicMapper topicMapper;
    private final CommentMapper commentMapper;

    public PostListModel convertToPostListModel(PostEntity postEntity) {
        return new PostListModel(
                postEntity.getUuid(),
                postEntity.getTitle(),
                postEntity.getContent(),
                this.userMapper.convertToPartialUserModel(postEntity.getUser()),
                this.topicMapper.convertToTopicModel(postEntity.getTopic()),
                postEntity.getCreatedAt(),
                postEntity.getUpdatedAt(),
                postEntity.getDeletedAt()
        );
    }

    public PostModel convertToPostModel(PostEntity postEntity) {
        return new PostModel(
                postEntity.getUuid(),
                postEntity.getTitle(),
                postEntity.getContent(),
                this.userMapper.convertToPartialUserModel(postEntity.getUser()),
                this.topicMapper.convertToTopicModel(postEntity.getTopic()),
                this.commentMapper.convertToCommentModelsList(postEntity.getComments()),
                postEntity.getCreatedAt(),
                postEntity.getUpdatedAt(),
                postEntity.getDeletedAt()
        );
    }
}
