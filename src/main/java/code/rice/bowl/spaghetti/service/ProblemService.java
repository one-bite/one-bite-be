package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.dto.request.CreateProblemRequest;
import code.rice.bowl.spaghetti.entity.Problem;
import code.rice.bowl.spaghetti.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;

    public boolean createProblem(CreateProblemRequest request) {

        try {
            problemRepository.save(Problem.builder()
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .answer(request.getAnswer())
                    .score(request.getScore())
                    .questionType(Problem.QuestionType.valueOf(request.getQuestionType()))
                    .difficulty(Problem.DifficultyLevel.valueOf(request.getDifficulty()))
                    .build());

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
