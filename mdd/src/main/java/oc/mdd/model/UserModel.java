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
    private String created_at;
    private String updated_at;
    private String deleted_at;

    public UserModel(String uuid, String email, String name, List<TopicEntity> topics, LocalDateTime created_at, LocalDateTime updated_at) {
        this.uuid = uuid;
        this.email = email;
        this.name = name;
        this.topics = topics;
        this.created_at = String.valueOf(created_at);
        this.updated_at = String.valueOf(updated_at);
        this.deleted_at = String.valueOf(deleted_at);
    }

    @Override
    public String toString() {
        return "User:{id=" + uuid + ", email=" + email + ", name=" + name + " topics" + topics + ", created_at=" + created_at + ", " +
                "updated_at=" + updated_at + "}";
    }
}