package oc.mdd.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

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
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column()
    private LocalDateTime updatedAt;

    @Column()
    private LocalDateTime deletedAt;

    @JsonIgnore
    @ManyToMany(mappedBy = "topics")
    private List<UserEntity> users;

    @JsonIgnore
    @OneToMany(mappedBy = "topic")
    private List<PostEntity> posts;


}
