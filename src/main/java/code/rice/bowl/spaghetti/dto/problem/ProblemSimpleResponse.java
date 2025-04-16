package code.rice.bowl.spaghetti.dto.problem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProblemSimpleResponse {
    private Long problemId;
    private String title;
    private int score;
}