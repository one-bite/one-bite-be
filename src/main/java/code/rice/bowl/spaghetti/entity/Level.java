package code.rice.bowl.spaghetti.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "levels")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Level {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long levelId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int minRating;

    @Column(nullable = false)
    private int maxRating;
}
