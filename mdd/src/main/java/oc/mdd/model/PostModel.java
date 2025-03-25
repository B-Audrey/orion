package oc.mdd.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class PostModel {
    private String uuid;
    private String title;
    private String content;
    private PartialUserModel user;
    private TopicModel topic;
    private List<CommentModel> comments;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;

    public PostModel(String uuid, String title, String content, PartialUserModel user, TopicModel topic,
            List<CommentModel> comments, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.uuid = uuid;
        this.title = title;
        this.content = content;
        this.user = user;
        this.topic = topic;
        this.comments = this.listCommentModel(comments);
        this.createdAt = String.valueOf(createdAt);
        this.updatedAt = String.valueOf(updatedAt);
        this.deletedAt = String.valueOf(deletedAt);
    }

    List<CommentModel> listCommentModel(List<CommentModel> comments) {
        if (comments == null) {
            return new ArrayList<>();
        } else {
            return comments;
        }
    }
}
