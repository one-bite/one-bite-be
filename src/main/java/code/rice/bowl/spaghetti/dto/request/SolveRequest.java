package code.rice.bowl.spaghetti.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
public class SolveRequest {
    private Long userId;
    private String answer;
    private int solveTime;
}
