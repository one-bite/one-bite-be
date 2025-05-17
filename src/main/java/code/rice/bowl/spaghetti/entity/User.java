package code.rice.bowl.spaghetti.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;

    // 초기에는 이메일에서 이메일 ID로 설정.
    @Column(nullable = false)
    private String username;

    private int rating;

    private int points;

    @Builder.Default
    private long courseId = 1;

    // 브론즈, 실버 같은 등급.
    @ManyToOne
    @JoinColumn(name = "rank_id")
    private Rank rank;

    private LocalDateTime createdAt;

    private LocalDateTime lastLogin;

    @Column(nullable = false)
    private boolean isNew;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserBadge> userBadges = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TodayProblem> todayProblems = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<NotSolveProblem> notSolveAiProblems = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Setter(AccessLevel.NONE)
    private Streak streak;

    public void setStreak(Streak steak) {
        this.streak = steak;

        if (this.streak.getUser() != this) {
            this.streak.setUser(this);
        }
    }

    /**
     * 문제 해결 성공시 호출 됨.
     */
    public void addPoints(int additionalPoints) {
        // 1. 포인트 증가
        this.points += additionalPoints;

        // 2. 스트릭 업데이트
//        LocalDateTime now = LocalDateTime.now();
//        int year = now.getYear();
//        int month = now.getMonthValue();
//        int day = now.getDayOfMonth();

//        this.streak.addActiveDate(year, month, day);
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        // TODO: 문제 풀 때 마다 사용자 Level 값을 갱신 함.
    }
}
