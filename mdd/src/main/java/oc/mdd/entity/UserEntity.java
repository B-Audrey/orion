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
import java.util.Set;

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
    @Column(name = "created_at", updatable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updated_at;

    @Column(name = "deleted_at")
    private LocalDateTime deleted_at;

    @ManyToMany(fetch = FetchType.EAGER) // warn, topics become too heavy, think about refactoring here.
    @JoinTable(
            name = "user_topics",
            joinColumns = @JoinColumn(name = "users_uuid"),
            inverseJoinColumns = @JoinColumn(name = "topics_uuid")
    )
    private List<TopicEntity> topics;

    @PreRemove
    protected void onDelete() {
        deleted_at = LocalDateTime.now();
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