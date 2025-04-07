package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.dto.response.SolveResponse;
import code.rice.bowl.spaghetti.entity.*;
import code.rice.bowl.spaghetti.exception.ProblemNotFoundException;
import code.rice.bowl.spaghetti.exception.UserNotFoundException;
import code.rice.bowl.spaghetti.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GradingService {

    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final UserProblemHistoryRepository historyRepository;

    public SolveResponse grade(Long problemId, Long userId, String submittedAnswer, int solveTime) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("cannot find a specific user"));

        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ProblemNotFoundException("cannot find a problem"));

    boolean isCorrect = normalize(submittedAnswer).equals(normalize(problem.getAnswer()));
        int score = isCorrect ? problem.getScore() : 0;

        if (isCorrect) {
            user.setPoints(user.getPoints() + score);
            userRepository.save(user);
        }

        UserProblemHistory history = UserProblemHistory.builder()
                .user(user)
                .problem(problem)
                .submittedAnswer(submittedAnswer)
                .isCorrect(isCorrect)
                .solveTime(solveTime)
                .build();
        historyRepository.save(history);

        return new SolveResponse(isCorrect, score);
    }

    //Trimming
    private String normalize(String str) {
        return str == null ? "" : str.trim().toLowerCase();
    }
}
