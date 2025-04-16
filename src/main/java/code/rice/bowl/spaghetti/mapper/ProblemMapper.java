package code.rice.bowl.spaghetti.mapper;

import code.rice.bowl.spaghetti.dto.problem.ProblemDto;
import code.rice.bowl.spaghetti.dto.problem.ProblemResponse;
import code.rice.bowl.spaghetti.dto.problem.ProblemSimpleResponse;
import code.rice.bowl.spaghetti.entity.Problem;
import code.rice.bowl.spaghetti.entity.Topic;

public class ProblemMapper {

    public static Problem toEntity(ProblemDto dto, Topic topic) {
        return Problem.builder()
                .topic(topic)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .questionType(dto.getQuestionType())
                .difficulty(dto.getDifficulty())
                .hint(dto.getHint())
                .answer(dto.getAnswer())
                .features(dto.getFeatures())
                .score(dto.getScore())
                .build();
    }

    public static ProblemResponse toDto(Problem problem) {
        return ProblemResponse.builder()
                .problemId(problem.getProblemId())
                .title(problem.getTitle())
                .description(problem.getDescription())
                .questionType(problem.getQuestionType())
                .difficulty(problem.getDifficulty())
                .hint(problem.getHint())
                .answer(problem.getAnswer())
                .features(problem.getFeatures())
                .score(problem.getScore())
                .build();
    }

    public static ProblemSimpleResponse toSimpleDto(Problem problem) {
        return ProblemSimpleResponse.builder()
                .problemId(problem.getProblemId())
                .title(problem.getTitle())
                .score(problem.getScore())
                .build();
    }
}