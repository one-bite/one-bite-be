package code.rice.bowl.spaghetti.dto.response;

import code.rice.bowl.spaghetti.dto.problem.ProblemDetailResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChallengeProblemResponse {
    int leftChance;
    int score;
    ProblemDetailResponse problem;
}
