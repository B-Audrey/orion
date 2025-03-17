package oc.mdd.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TopicModel {
    private String id;
    private String label;
    private String description;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;

    public TopicModel(String id, String name, String description, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.label = name;
        this.description = description;
        this.createdAt = String.valueOf(createdAt);
        this.updatedAt = String.valueOf(updatedAt);
        this.deletedAt = String.valueOf(deletedAt);
    }

    @Override
    public String toString() {
        return "Topic:{id=" + id + ", name=" + label + ", description=" + description + ", createdAt=" + createdAt + ", " +
                "updatedAt=" + updatedAt + "}";
    }
}
