package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.entity.TodayProblem;
import code.rice.bowl.spaghetti.entity.User;
import code.rice.bowl.spaghetti.exception.NotFoundException;
import code.rice.bowl.spaghetti.repository.TodayProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodayProblemService {
    private final TodayProblemRepository todayProblemRepository;
    private final UserService userService;
    /**
     * 오늘 문제에 대하여 제출 여부를 설정함.
     *
     * @param userId        사용자 id.
     * @param problemId     오늘 해결한 문제 id.
     */
    @Transactional
    public void setSubmit(Long userId, Long problemId) {
        TodayProblem todayProblem = todayProblemRepository.findByUser_IdAndProblem_Id(userId, problemId)
                .orElseThrow(() -> new NotFoundException("not today problem"));

        todayProblem.setSubmitYN(true);

//        todayProblemRepository.save(todayProblem);
    }

    /**
     * 오늘의 문제에 대하여 모두 해결 여부를 나타냄.
     * @param userId    사용자 id
     * @return  true (모든 문제에 대한 제출 기록 존재), false (한 문제이라도 제출 기록이 없음)
     */
    public boolean allSolve(Long userId) {
        User user = userService.getUser(userId);

        List<TodayProblem> problems = user.getTodayProblems();

        // 한 문제이라도 제출 기록이 없으면 false.
        for (TodayProblem t: problems) {
            if (!t.isSubmitYN()) {
                return false;
            }
        }

        return true;
    }
}
