package code.rice.bowl.spaghetti.mapper;

import code.rice.bowl.spaghetti.dto.userproblemhistory.UserProblemHistoryResponse;
import code.rice.bowl.spaghetti.entity.UserProblemHistory;

public class UserProblemHistoryMapper {

    public static UserProblemHistoryResponse toDto(UserProblemHistory history) {
        return UserProblemHistoryResponse.builder()
                .historyId(history.getHistoryId())
                .userId(history.getUser().getUserId())
                .problem(ProblemMapper.toSimpleDto(history.getProblem()))
                .submittedAnswer(history.getSubmittedAnswer())
                .isCorrect(history.getIsCorrect())
                .solveTime(history.getSolveTime())
                .submittedAt(history.getSubmittedAt())
                .build();
    }
}