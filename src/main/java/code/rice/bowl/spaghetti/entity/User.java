package code.rice.bowl.spaghetti.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
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
    private String username;

    private int rating;

    @Setter
    private int points;

    // 브론즈, 실버 같은 등급.
    @ManyToOne
    @JoinColumn(name = "level_id")
    private Level level;

    private LocalDateTime createdAt;

    private LocalDateTime lastLogin;

    @Getter(AccessLevel.NONE)
    @Column(nullable = false)
    private boolean isNew;

    public boolean isNew() {
        return isNew;
    }

    public void addPoints(int additionalPoints) {
        this.points += additionalPoints;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
