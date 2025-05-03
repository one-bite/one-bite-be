package code.rice.bowl.spaghetti.dto.user;

import code.rice.bowl.spaghetti.dto.problem.ProblemDetailResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 사용자의 오늘 문제 리스트를 요청할 때 사용.
 */
@Getter
@Setter
@Builder
public class UserTodayProblemResponse {
    List<Boolean> problemStatus;
    List<ProblemDetailResponse> problemList;
}
