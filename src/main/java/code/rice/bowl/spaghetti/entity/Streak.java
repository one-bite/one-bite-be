package code.rice.bowl.spaghetti.entity;

import code.rice.bowl.spaghetti.utils.DateUtils;
import code.rice.bowl.spaghetti.utils.HashSetConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
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

    @JdbcTypeCode(SqlTypes.JSON)
    @Convert(converter = HashSetConverter.class)
    @Builder.Default
    private Set<String> activeDates = new HashSet<>();

    public void addActiveDate(String today) {
        // 0. 오늘의 스트릭을 이미 추가한 경우 -> 리턴
        if (today.equals(updatedAt))
            return;

        // 1. 오늘이미 해결 했음을 저장 함.
        activeDates.add(today);

        // 2. 최대 스트릭과 현재 스트릭 계산.
        if (DateUtils.diffOneDay(updatedAt, today)) {
            nowStreakCount += 1;
        } else {
            nowStreakCount = 1;
        }

        // 3. 최대 스트릭 갱신.
        maxStreakCount = Math.max(nowStreakCount, maxStreakCount);

        // 4. 마지막 제출일을 오늘로 설정.
        updatedAt = today;
    }

    @Builder.Default
    private String updatedAt = "1999-01-01";

    private int maxStreakCount;

    private int nowStreakCount;
}
