package code.rice.bowl.spaghetti.dto.streak;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class StreakInfoResponse {
    int maxStreak;
    int nowStreak;
    Set<String> streakHistory;
}
