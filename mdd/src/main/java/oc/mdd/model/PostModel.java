package oc.mdd.model;

import lombok.Data;
import oc.mdd.entity.TopicEntity;
import oc.mdd.service.PostService;
import oc.mdd.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@Data
public class PostModel {
    private String uuid;
    private String title;
    private String content;
    private UserModel user;
    private TopicModel topic;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;

    public PostModel(String uuid, String title, String content, TopicModel topic, UserModel user, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
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
