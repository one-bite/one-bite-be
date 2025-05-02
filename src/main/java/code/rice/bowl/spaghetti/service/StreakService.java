package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.dto.streak.StreakInfoResponse;
import code.rice.bowl.spaghetti.entity.User;
import code.rice.bowl.spaghetti.mapper.StreakMapper;
import code.rice.bowl.spaghetti.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        User nowUser = userService.getUser(userId);

        nowUser.getStreak().addActiveDate(DateUtils.today());
    }

    /**
     * 사용자라 스트릭 정보 리턴
     * @param email 현재 사용자 이메일 정보
     * @return  사용자의 스트릭 정보.
     */
    public StreakInfoResponse getStreak(String email) {
        return StreakMapper.toDto(userService.getUser(email).getStreak());
    }
}
