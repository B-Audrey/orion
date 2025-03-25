package oc.mdd.service;

import lombok.RequiredArgsConstructor;
import oc.mdd.dto.PaginationQueryDto;
import oc.mdd.dto.PostCreationDto;
import oc.mdd.entity.CommentEntity;
import oc.mdd.entity.PostEntity;
import oc.mdd.entity.TopicEntity;
import oc.mdd.entity.UserEntity;
import oc.mdd.repository.CommentRepository;
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
    private final CommentRepository commentRepository;
    private final TopicService topicService;

    /**
     * Find a post by its uuid
     *
     * @param uuid the post uuid
     * @return the post
     */
    public PostEntity findByUuidWithComments(String uuid) {
        return this.postRepository.findByUuidWithComments(uuid);
    }

    /**
     * Create a post
     *
     * @param post the post dto containing the post data and the topic uuid
     * @param user the user to link
     * @return the created post
     */
    public PostEntity createPost(PostCreationDto post, UserEntity user) {
        TopicEntity topicEntity = this.topicService.getTopicByUuid(post.getTopicUuid());
        PostEntity postEntity = new PostEntity();
        postEntity.setTitle(post.getTitle());
        postEntity.setContent(post.getContent());
        postEntity.setTopic(topicEntity);
        postEntity.setUser(user);
        return this.postRepository.save(postEntity);
    }

    /**
     * Get the user feed
     *
     * @param uuid    the user uuid
     * @param pageDto the page params
     * @return the user feed
     */
    public Page<PostEntity> getUserFeed(String uuid, PaginationQueryDto pageDto) {
        Pageable pageable = PaginationUtil.createPageable(pageDto.getPage(), pageDto.getSize(), pageDto.getSort());
        return postRepository.findUserFeed(uuid, pageable);

    }

    /**
     * Post a comment on a post
     *
     * @param postUuid       the post uuid
     * @param commentContent the comment content
     * @param user           the user
     * @return the post with the new comment
     */
    public PostEntity postCommentOnPost(String postUuid, String commentContent, UserEntity user) throws Exception {
        PostEntity post = this.postRepository.findByUuid(postUuid);
        if (post == null) {
            throw new Exception("post not found");
        }
        CommentEntity newComment = new CommentEntity();
        newComment.setPost(post);
        newComment.setUser(user);
        newComment.setContent(commentContent);
        this.commentRepository.save(newComment);
        return this.postRepository.findByUuidWithComments(postUuid);
    }

}

