package oc.mdd.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostListModel {
    private String uuid;
    private String title;
    private String content;
    private PartialUserModel user;
    private TopicModel topic;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;

    public PostListModel(String uuid, String title, String content, PartialUserModel user,  TopicModel topic, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.uuid = uuid;
        this.title = title;
        this.content = content;
        this.user = user;
        this.topic = topic;
        this.createdAt = String.valueOf(createdAt);
        this.updatedAt = String.valueOf(updatedAt);
        this.deletedAt = String.valueOf(deletedAt);
    }
}
