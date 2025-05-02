package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StreakService {

    private final TodayProblemService todayProblemService;
    private final UserService userService;

    /**
     * 사용자의 현재 스트릭을 업데이트 수행.
     *
     * @param userId        사용자 id
     */
    @Transactional
    public void updateStreak(Long userId) {
        // 1. 오늘의 문제에 대하여 모든 문제에 대한 제출 여부를 체크.
        if (!todayProblemService.allSolve(userId))
            return;

        // 2. 스트릭 업데이트
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();

        User nowUser = userService.getUser(userId);

        nowUser.getStreak().addActiveDate(year, month, day);
    }
}
