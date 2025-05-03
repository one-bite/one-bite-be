package code.rice.bowl.spaghetti.mapper;

import code.rice.bowl.spaghetti.dto.problem.ProblemDetailResponse;
import code.rice.bowl.spaghetti.dto.problem.ProblemRequest;
import code.rice.bowl.spaghetti.dto.problem.ProblemResponse;
import code.rice.bowl.spaghetti.dto.problem.ProblemSimpleResponse;
import code.rice.bowl.spaghetti.entity.Category;
import code.rice.bowl.spaghetti.entity.Problem;
import code.rice.bowl.spaghetti.entity.Topic;
import code.rice.bowl.spaghetti.entity.User;

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

    public static ProblemResponse toDto(Problem problem) {
        Long categoryId = problem.getCategory() != null ? problem.getCategory().getCategoryId() : null;
        Long userId = problem.getUser() != null ? problem.getUser().getUserId() : null;
        Long[] topicIds = problem.getTopics() != null ?
                problem.getTopics().stream().map(Topic::getTopicId).toArray(Long[]::new) : new Long[]{};

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
                .topicIds(topicIds)
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
