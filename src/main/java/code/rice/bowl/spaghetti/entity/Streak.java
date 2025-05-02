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
        String today = year + "-" + month + "-" + day;

        // 0. 오늘의 스트릭을 이미 추가한 경우 -> 리턴
        if (today.equals(updatedAt))
            return;

        // 1. 오늘이미 해결 했음을 저장 함.
        activeDates.add(today);

        // 2. 최대 스트릭과 현재 스트릭 계산.
        LocalDate now = LocalDate.parse(today);
        LocalDate last = LocalDate.parse(updatedAt);

        // 2-1. 마지막 푼 날과 하루 차이 인지 확인.
        if (last.plusDays(1).equals(now)) {
            nowStreakCount += 1;
        } else {
            nowStreakCount = 0;
        }

        // 최대 스트릭 갱신.
        maxStreakCount = Math.max(nowStreakCount, maxStreakCount);

        // 마지막 제출일을 오늘로 설정.
        updatedAt = today;
    }

    private String updatedAt;

    private int maxStreakCount;

    private int nowStreakCount;
}
