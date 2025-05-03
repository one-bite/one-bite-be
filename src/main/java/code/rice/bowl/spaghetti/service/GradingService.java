package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.dto.response.SubmitResponse;
import code.rice.bowl.spaghetti.entity.Problem;
import code.rice.bowl.spaghetti.entity.User;
import code.rice.bowl.spaghetti.entity.UserProblemHistory;
import code.rice.bowl.spaghetti.exception.NotFoundException;
import code.rice.bowl.spaghetti.repository.ProblemRepository;
import code.rice.bowl.spaghetti.repository.UserProblemHistoryRepository;
import code.rice.bowl.spaghetti.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GradingService {

    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final UserProblemHistoryRepository historyRepository;

    private final StreakService streakService;
    private final TodayProblemService todayProblemService;

    public SubmitResponse grade(Long problemId, Long userId, String submittedAnswer, int solveTime) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("cannot find a specific user"));

        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new NotFoundException("cannot find a problem"));

        boolean isCorrect = normalize(submittedAnswer).equals(normalize(problem.getAnswer()));

        int score = isCorrect ? problem.getScore() : 0;

        if (isCorrect) {
            user.addPoints(score);
            userRepository.save(user);
        }

        // 1. 오늘 해당 문제에 대하여 제출하였음을 변경.
        todayProblemService.setSubmit(userId, problemId);

        // 2. 오늘 스트릭 증가 여부를 처리.
        streakService.updateStreak(userId);

        UserProblemHistory history = UserProblemHistory.builder()
                .user(user)
                .problem(problem)
                .submittedAnswer(submittedAnswer)
                .isCorrect(isCorrect)
                .solveTime(solveTime)
                .build();
        historyRepository.save(history);

        return new SubmitResponse(isCorrect, score);
    }

    //Trimming
    private String normalize(String str) {
        return str == null ? "" : str.trim().toLowerCase();
    }
}