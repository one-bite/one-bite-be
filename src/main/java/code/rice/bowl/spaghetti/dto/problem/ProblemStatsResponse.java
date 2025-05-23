package code.rice.bowl.spaghetti.dto.problem;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProblemStatsResponse {
    private final long total;   // user가 null인 전체 문제 수
    private final long solved;  // user가 null인 문제 중 내가 푼 수
}
