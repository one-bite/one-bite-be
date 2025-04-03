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

    private String username;

    private int rating;

    private int points;

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

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
