// src/main/java/code/rice/bowl/spaghetti/service/GradingService.java
package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.dto.ai.AiProblemRequest;
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

    /**
     * 문제 채점 및 오답 시 AI 문제 자동 생성
     */
    @Transactional
    public SubmitResponse grade(Long problemId, Long userId, String submittedAnswer, int solveTime) {
        // 1) 오늘 할당된 문제인지 검증
        User userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        boolean assigned = todayProblemService.getUserTodayProblems(userEntity.getEmail())
                .getProblemList().stream()
                .anyMatch(p -> p.getProblemId().equals(problemId));
        if (!assigned) {
            throw new NotFoundException("This problem is not assigned for Today");
        }

        // 2) 사용자, 문제 조회
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new NotFoundException("Problem not found"));

        // 3) 정답 체크
        boolean isCorrect = normalize(submittedAnswer).equals(normalize(problem.getAnswer()));
        int point = isCorrect ? problem.getPoint() : 0;

        // 1. 장답 체크.
        if (isCorrect) {
            userEntity.addPoints(point);
            userRepository.save(userEntity);
            todayProblemService.setSubmit(userId, problemId);
            streakService.updateStreak(userId);
        } else {
            // 오답 시 AI 문제 생성 (내일 할당)
            AiProblemRequest aiReq = new AiProblemRequest();
            aiReq.setParentProblemId(problemId);
            aiReq.setDescription(problem.getDescription().toString());
            aiReq.setTopics(Collections.singletonList(problem.getTopics().get(0).getCode()));
            aiReq.setQuestionType(problem.getQuestionType().name());
            aiReq.setUserId(userId);
            aiReq.setCategoryId(problem.getCategory().getCategoryId());
            aiReq.setCount(1);
            aiService.generateProblemAsync(aiReq);
        }

        // 5) 이력 저장
        historyRepository.save(UserProblemHistory.builder()
                .user(userEntity)
                .problem(problem)
                .submittedAnswer(submittedAnswer)
                .isCorrect(isCorrect)
                .solveTime(solveTime)
                .build());

        return new SubmitResponse(isCorrect, point);
    }

    private String normalize(String str) {
        return str == null ? "" : str.trim().toLowerCase();
    }
}
