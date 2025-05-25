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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class GradingService {

    private final AuthService authService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final UserProblemHistoryRepository historyRepository;
    private final StreakService streakService;
    private final TodayProblemService todayProblemService;
    private final AiService aiService;
    private final ProblemService problemService;
    private final ProblemRelationService problemRelationService;

    /**
     * 문제 채점 및 오답 시 AI 문제 자동 생성
     */
    @Transactional
    public SubmitResponse gradeTodayProblem(SubmitRequest request) {
        // 1) 인증된 사용자 정보
        Long userId = authService.getCurrentUserId();
        User user = userService.getUser(userId);

        // 2) 오늘의 문제 배정 여부 검사
        boolean assigned = todayProblemService
                .getUserTodayProblems(user.getEmail())
                .getProblemList().stream()
                .anyMatch(p -> p.getProblemId().equals(request.getProblemId()));
        if (!assigned) {
            throw new NotFoundException("This problem is not assigned for Today");
        }

        // 3) 문제 조회 및 채점
        Problem problem = problemRepository.findById(request.getProblemId())
                .orElseThrow(() -> new NotFoundException("Cannot find a problem"));
        boolean isCorrect = normalize(request.getAnswer())
                .equals(normalize(problem.getAnswer()));
        int point = isCorrect ? problem.getPoint() : 0;

        // 4) 정답/오답 처리
        if (isCorrect) {
            user.addPoints(point);
            userRepository.save(user);
        } else {
            // 오답 시 AI 문제 생성 요청
            AiProblemRequest aiReq = new AiProblemRequest();
            aiReq.setParentProblemId(problem.getProblemId());
            aiReq.setDescription(problem.getDescription());
            aiReq.setTopics(Collections.singletonList(
                    problem.getTopics().get(0).getCode()
            ));
            aiReq.setQuestionType(problem.getQuestionType().name());

            // 비동기 호출
            aiService.generateProblemRequestAsync(aiReq)
                    .thenApply(reqDto -> problemService.create(reqDto))
                    .thenAccept(problemResp ->
                            problemRelationService.createRelation(
                                    problem.getProblemId(),
                                    problemResp.getProblemId()
                            )
                    );
        }

        // 5) 제출 상태 저장 및 스트릭 업데이트
        todayProblemService.setSubmit(userId, problem.getProblemId());
        streakService.updateStreak(userId);

        // 6) 이력 저장
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
