package oc.mdd.model;

import lombok.Data;

@Data
public class TopicModel {
    private String id;
    private String name;
    private String description;
    private String created_at;
    private String updated_at;
    private String deleted_at;

    public TopicModel(String id, String name, String description, String created_at, String updated_at, String deleted_at) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
    }

    @Override
    public String toString() {
        return "Topic:{id=" + id + ", name=" + name + ", description=" + description + ", created_at=" + created_at + ", " +
                "updated_at=" + updated_at + "}";
    }
}
