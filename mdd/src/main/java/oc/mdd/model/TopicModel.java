package oc.mdd.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TopicModel {
    private String id;
    private String name;
    private String description;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;

    public TopicModel(String id, String name, String description, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = String.valueOf(createdAt);
        this.updatedAt = String.valueOf(updatedAt);
        this.deletedAt = String.valueOf(deletedAt);
    }

    @Override
    public String toString() {
        return "Topic:{id=" + id + ", name=" + name + ", description=" + description + ", createdAt=" + createdAt + ", " +
                "updatedAt=" + updatedAt + "}";
    }
}
