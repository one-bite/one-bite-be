package code.rice.bowl.spaghetti.mapper;


import code.rice.bowl.spaghetti.dto.streak.StreakInfoResponse;
import code.rice.bowl.spaghetti.entity.Streak;

public class StreakMapper {

    public static StreakInfoResponse toDto(Streak streak) {
        return StreakInfoResponse.builder()
                .maxStreak(streak.getMaxStreakCount())
                .nowStreak(streak.getNowStreakCount())
                .streakHistory(streak.getActiveDates())
                .build();
    }
}
