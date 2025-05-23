// src/main/java/code/rice/bowl/spaghetti/entity/ProblemRelation.java
package code.rice.bowl.spaghetti.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 문제 간 부모-자식(파생) 관계를 저장하는 테이블
 */
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "problem_relation")
public class ProblemRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 부모 문제 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_problem_id", nullable = false)
    private Problem parentProblem;

    /** AI(자식) 문제 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_problem_id", nullable = false)
    private Problem childProblem;

    /** 생성 일시 */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
