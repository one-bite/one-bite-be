package code.rice.bowl.spaghetti.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "not_solve_problem")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotSolveProblem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notSolveProblemId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "problem_id")
    private Problem problem;
}
