package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.dto.request.SubmitRequest;
import code.rice.bowl.spaghetti.dto.response.SubmitResponse;
import code.rice.bowl.spaghetti.entity.Problem;
import code.rice.bowl.spaghetti.entity.User;
import code.rice.bowl.spaghetti.entity.UserProblemHistory;
import code.rice.bowl.spaghetti.exception.NotFoundException;
import code.rice.bowl.spaghetti.repository.ProblemRepository;
import code.rice.bowl.spaghetti.repository.UserProblemHistoryRepository;
import code.rice.bowl.spaghetti.repository.UserRepository;
import jakarta.transaction.Transactional;
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
    private final UserService userService;


    @Transactional
    public SubmitResponse gradeTodayProblem(String email, SubmitRequest request) {
        boolean assigned = todayProblemService
                .getUserTodayProblems(email)
                .getProblemList().stream()
                .anyMatch(p -> p.getProblemId().equals(request.getProblemId()));

        if (!assigned) {
            // 오늘 할당된 TodayProblem 아닐 때
            throw new NotFoundException("This problem is not assigned for Today");
        }

        User user = userService.getUser(email);

        Problem problem = problemRepository.findById(request.getProblemId())
                .orElseThrow(() -> new NotFoundException("cannot find a problem"));

        boolean isCorrect = normalize(request.getAnswer())
                .equals(normalize(problem.getAnswer()));
        int point = isCorrect ? problem.getPoint() : 0;

        // 1. 장답 체크.
        if (isCorrect) {
            user.addPoints(point);
            userRepository.save(user);
        }

        // 2. 제출 체크
        todayProblemService.setSubmit(user.getUserId(), problem.getProblemId());

        // 3. 스트릭 업데이트
        streakService.updateStreak(user.getUserId());

        // 4. 이력 저장
        UserProblemHistory history = UserProblemHistory.builder()
                .user(user)
                .problem(problem)
                .submittedAnswer(request.getAnswer())
                .isCorrect(isCorrect)
                .solveTime(request.getSolveTime())
                .build();
        historyRepository.save(history);

        return new SubmitResponse(isCorrect, point);
    }

    //Trimming
    private String normalize(String str) {
        return str == null ? "" : str.trim().toLowerCase();
    }
}