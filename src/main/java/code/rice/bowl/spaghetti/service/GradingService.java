package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.dto.ai.AiProblemRequest;
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

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class GradingService {

    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final UserProblemHistoryRepository historyRepository;
    private final StreakService streakService;
    private final TodayProblemService todayProblemService;
    private final AiService aiService;
    private final UserService userService;

    /**
     * 문제 채점 및 오답 시 AI 문제 자동 생성
     */
    @Transactional
    public SubmitResponse gradeTodayProblem(String email, SubmitRequest request) {
        // 1. 오늘의 문제에 포함되어 있는지 확인
        boolean assigned = todayProblemService
                .getUserTodayProblems(email)
                .getProblemList().stream()
                .anyMatch(p -> p.getProblemId().equals(request.getProblemId()));

        if (!assigned) {
            throw new NotFoundException("This problem is not assigned for Today");
        }

        // 2. 사용자 및 문제 조회
        User user = userService.getUser(email);
        Problem problem = problemRepository.findById(request.getProblemId())
                .orElseThrow(() -> new NotFoundException("Cannot find a problem"));

        // 3. 정답 여부 판단
        boolean isCorrect = normalize(request.getAnswer())
                .equals(normalize(problem.getAnswer()));
        int point = isCorrect ? problem.getPoint() : 0;

        // 4. 정답 처리
        if (isCorrect) {
            user.addPoints(point);
            userRepository.save(user);
        } else {
            // 5. 오답일 경우 AI 문제 생성 요청
            AiProblemRequest aiReq = new AiProblemRequest();
            aiReq.setParentProblemId(problem.getProblemId());
            aiReq.setDescription(problem.getDescription().toString());
            aiReq.setTopics(Collections.singletonList(problem.getTopics().get(0).getCode()));
            aiReq.setQuestionType(problem.getQuestionType().name());
            aiReq.setUserId(user.getUserId());
            aiReq.setCategoryId(problem.getCategory().getCategoryId());
            aiService.generateProblemRequestAsync(aiReq);
        }

        // 6. 제출 여부 저장 및 스트릭 업데이트
        todayProblemService.setSubmit(user.getUserId(), problem.getProblemId());
        streakService.updateStreak(user.getUserId());

        // 7. 이력 저장
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

    private String normalize(String str) {
        return str == null ? "" : str.trim().toLowerCase();
    }
}
