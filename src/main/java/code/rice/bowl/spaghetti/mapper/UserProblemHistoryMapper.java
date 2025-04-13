package code.rice.bowl.spaghetti.mapper;

import code.rice.bowl.spaghetti.dto.response.UserProblemHistoryResponse;
import code.rice.bowl.spaghetti.entity.UserProblemHistory;

public class UserProblemHistoryMapper {

    public static UserProblemHistoryResponse toDto(UserProblemHistory history) {
        return UserProblemHistoryResponse.builder()
                .historyId(history.getHistoryId())
                .userId(history.getUser().getUserId())
                .problemId(history.getProblem().getProblemId())
                .submittedAnswer(history.getSubmittedAnswer())
                .isCorrect(history.getIsCorrect())
                .solveTime(history.getSolveTime())
                .submittedAt(history.getSubmittedAt())
                .build();
    }
}