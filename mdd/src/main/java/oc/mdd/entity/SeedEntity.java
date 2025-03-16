package oc.mdd.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "seeds")
@Data
public class SeedEntity {

    @Id
    private int id; // use voluntary ids here for manual generation tha would be impossible with uuids

    @Column(nullable = false)
    private String name;
}
