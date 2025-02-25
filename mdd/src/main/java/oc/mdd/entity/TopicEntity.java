package oc.mdd.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@RequiredArgsConstructor
@Table(name = "topics")
@EntityListeners(AuditingEntityListener.class) // Pour les champs created_at et updated_at
public class TopicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    @Column(nullable = false, unique = true)
    private String label;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updated_at;

    @Column(name = "deleted_at")
    private LocalDateTime deleted_at;

    @ManyToMany
    @JoinTable(
            name = "topics_users",
            joinColumns = @JoinColumn(name = "topics_uuid"),
            inverseJoinColumns = @JoinColumn(name = "users_uuid")
    )
    private Set<UserEntity> users;
}
