package oc.mdd.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "seeds")
@Data
public class SeedEntity {

    @Id
    private int id;

    @Column(nullable = false)
    private String name;
}
