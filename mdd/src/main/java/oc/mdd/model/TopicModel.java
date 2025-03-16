package oc.mdd.model;

import lombok.Data;

@Data
public class TopicModel {
    private String id;
    private String name;
    private String description;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;

    @Override
    public String toString() {
        return "Topic:{id=" + id + ", name=" + name + ", description=" + description + ", createdAt=" + createdAt + ", " +
                "updatedAt=" + updatedAt + "}";
    }
}
