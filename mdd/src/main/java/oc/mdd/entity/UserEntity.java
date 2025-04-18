package oc.mdd.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@RequiredArgsConstructor
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class) // Pour les champs created_at et updated_at
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String name;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column()
    private LocalDateTime updatedAt;

    @Column()
    private LocalDateTime deletedAt;

    @ManyToMany()
    @JoinTable(
            name = "user_topics",
            joinColumns = @JoinColumn(name = "users_uuid"),
            inverseJoinColumns = @JoinColumn(name = "topics_uuid")
    )
    private List<TopicEntity> topics;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<PostEntity> posts;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<CommentEntity> comments;

    @PreRemove
    protected void onDelete() {
        deletedAt = LocalDateTime.now();
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER")); // unused for now, no roles, everyone is user
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return email; // use Email as name to find user
    }
}