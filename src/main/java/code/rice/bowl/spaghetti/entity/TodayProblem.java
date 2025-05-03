package code.rice.bowl.spaghetti.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 각 사용자의 오늘의 풀어 할 문제를 저장하는 테이블
 */
@Entity
@Getter
@Setter
@Builder
@Table(
        name = "today_problems",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "problem_id"})}
)
@NoArgsConstructor
@AllArgsConstructor
public class TodayProblem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long todayProblemId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @Column(name = "submit_yn")
    @Builder.Default
    public boolean submitYN = false;
}
