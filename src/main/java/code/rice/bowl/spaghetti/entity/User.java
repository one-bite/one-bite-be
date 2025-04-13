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
    @Column(nullable = false, unique = true)
    private String username;

    private int rating;

    private int points;

    // 브론즈, 실버 같은 등급.
    @ManyToOne
    @JoinColumn(name = "level_id")
    private Level level;

    private LocalDateTime createdAt;

    private LocalDateTime lastLogin;

    @Column(nullable = false)
    private boolean isNew;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserBadge> userBadges = new ArrayList<>();

    public void addPoints(int additionalPoints) {
        this.points += additionalPoints;
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
