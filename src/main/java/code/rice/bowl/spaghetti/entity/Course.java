package code.rice.bowl.spaghetti.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "courses")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @OneToOne
    @JoinColumn(name = "problem_id")
    private Problem problem;
}
