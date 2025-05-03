package code.rice.bowl.spaghetti.mapper;

import code.rice.bowl.spaghetti.dto.problem.ProblemDetailResponse;
import code.rice.bowl.spaghetti.dto.ai.AiProblemRequest;
import code.rice.bowl.spaghetti.dto.ai.AiProblemResponse;
import code.rice.bowl.spaghetti.dto.problem.ProblemRequest;
import code.rice.bowl.spaghetti.dto.problem.ProblemResponse;
import code.rice.bowl.spaghetti.dto.problem.ProblemSimpleResponse;
import code.rice.bowl.spaghetti.entity.Category;
import code.rice.bowl.spaghetti.entity.Problem;
import code.rice.bowl.spaghetti.entity.Problem.DifficultyLevel;
import code.rice.bowl.spaghetti.entity.Problem.QuestionType;
import code.rice.bowl.spaghetti.entity.Topic;
import code.rice.bowl.spaghetti.entity.User;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ProblemMapper {

    public static Problem toEntity(ProblemRequest dto, Category category, List<Topic> topics, User user) {
        return Problem.builder()
                .category(category)
                .topics(topics)
                .user(user)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .questionType(dto.getQuestionType())
                .hint(dto.getHint())
                .answer(dto.getAnswer())
                .point(dto.getPoint())
                .build();
    }

    public static Problem toEntity(AiProblemResponse aiResDto, Topic topic) {
        return Problem.builder()
                .topic(topic)
                // AI에서 받은 'problemContent'를 제목과 본문(description)에 동일하게 사용
                .title(aiResDto.getProblemContent())
                .description(aiResDto.getProblemContent())
                // 문자열을 Enum으로 변환
                .questionType(QuestionType.valueOf(aiResDto.getQuestionType().toUpperCase()))
                .difficulty(DifficultyLevel.valueOf(aiResDto.getDifficulty().toUpperCase()))
                // AI 응답 DTO에 hint, answer, features, score 필드가 없으므로 기본값 또는 null
                .hint(null)
                .answer(null)
                .features(null)
                .score(0)
                .build();
    }

    public static ProblemResponse toDto(Problem problem) {
        Long categoryId = problem.getCategory() != null ? problem.getCategory().getCategoryId() : null;
        Long userId = problem.getUser() != null ? problem.getUser().getUserId() : null;
        List<String> topicNames = (problem.getTopics() != null && !problem.getTopics().isEmpty())
                ? problem.getTopics().stream().map(Topic::getName).collect(Collectors.toList())
                : Collections.emptyList();

        return ProblemResponse.builder()
                .problemId(problem.getProblemId())
                .title(problem.getTitle())
                .description(problem.getDescription())
                .questionType(problem.getQuestionType())
                .hint(problem.getHint())
                .answer(problem.getAnswer())
                .point(problem.getPoint())
                .categoryId(categoryId)
                .userId(userId)
                .topicNames(topicNames)
                .build();
    }

    public static ProblemSimpleResponse toSimpleDto(Problem problem) {
        return ProblemSimpleResponse.builder()
                .problemId(problem.getProblemId())
                .title(problem.getTitle())
                .score(problem.getPoint())
                .build();
    }

    public static ProblemDetailResponse toDetailDto(Problem problem) {
        return ProblemDetailResponse.builder()
                .title(problem.getTitle())
                .problemId(problem.getProblemId())
                .description(problem.getDescription())
                .build();
    }
}
