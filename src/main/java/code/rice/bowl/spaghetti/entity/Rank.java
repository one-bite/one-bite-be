package code.rice.bowl.spaghetti.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "ranks")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rankId;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private int minRating;

    @Column(nullable = false)
    private int maxRating;
}
