package oc.mdd.model;

import lombok.Data;
import oc.mdd.entity.TopicEntity;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserModel {
    private String uuid;
    private String email;
    private String name;
    private List<TopicEntity> topics;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;

    public UserModel(String uuid, String email, String name, List<TopicEntity> topics, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.uuid = uuid;
        this.email = email;
        this.name = name;
        this.topics = topics;
        this.createdAt = String.valueOf(createdAt);
        this.updatedAt = String.valueOf(updatedAt);
        this.deletedAt = String.valueOf(deletedAt);
    }

    @Override
    public String toString() {
        return "User:{id=" + uuid + ", email=" + email + ", name=" + name + " topics" + topics + ", createdAt=" + createdAt + ", " +
                "updatedAt=" + updatedAt + "}";
    }
}