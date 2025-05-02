package code.rice.bowl.spaghetti.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
public class TodayProblem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long todayProblemId;

    @Column(nullable = false)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    @ManyToOne
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @Column(name = "submit_yn")
    @Builder.Default
    public boolean submitYN = false;
}
