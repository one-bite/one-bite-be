package code.rice.bowl.spaghetti.dto.request;

import lombok.Getter;

@Getter
public class SubmitRequest {
    private Long userId;
    private String answer;
    private int solveTime;
}
