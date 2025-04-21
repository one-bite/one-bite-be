package code.rice.bowl.spaghetti.entity;

import code.rice.bowl.spaghetti.utils.HashSetConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "streaks")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Streak {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long streakId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @Column(columnDefinition = "jsonb")
    @Convert(converter = HashSetConverter.class)
    private Set<String> activeDates;

    public void addActiveDate(int year, int month , int day) {
        activeDates.add(year + "-" + month + "-" + day);

        updatedAt = LocalDateTime.now();
    }

    private LocalDateTime updatedAt;

    private int maxStreakCount;

    private int nowStreakCount;

    @PrePersist
    protected void onCreate() {
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
