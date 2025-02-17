package oc.mdd.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserModel {
    private String uuid;
    private String email;
    private String name;
    private String created_at;
    private String updated_at;
    private String deleted_at;

    public UserModel(String uuid, String email, String name, LocalDateTime created_at, LocalDateTime updated_at) {
        this.uuid = uuid;
        this.email = email;
        this.name = name;
        this.created_at = String.valueOf(created_at);
        this.updated_at = String.valueOf(updated_at);
        this.deleted_at = String.valueOf(deleted_at);
    }

    @Override
    public String toString() {
        return "User:{id=" + uuid + ", email=" + email + ", name=" + name + ", created_at=" + created_at + ", " +
                "updated_at=" + updated_at + "}";
    }
}