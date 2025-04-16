package code.rice.bowl.spaghetti.mapper;

import code.rice.bowl.spaghetti.dto.userprogress.UserProgressResponse;
import code.rice.bowl.spaghetti.entity.UserProgress;

public class UserProgressMapper {

    public static UserProgressResponse toUserProgressResponse(UserProgress userProgress) {
        return UserProgressResponse.builder()
                .topicName(userProgress.getTopic().getName())
                .totalProblem(userProgress.getTopic().getTotal())
                .solveCount(userProgress.getCompletedProblems())
                .build();
    }
}
