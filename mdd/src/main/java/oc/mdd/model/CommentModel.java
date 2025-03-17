package oc.mdd.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentModel {

    private String uuid;
    private String content;
    private PartialUserModel user;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;

    public CommentModel(String uuid, String content, PartialUserModel user, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.uuid = uuid;
        this.content = content;
        this.user = user;
        this.createdAt = String.valueOf(createdAt);
        this.updatedAt = String.valueOf(updatedAt);
        this.deletedAt = String.valueOf(deletedAt);
    }

}
