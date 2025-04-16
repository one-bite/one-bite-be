package code.rice.bowl.spaghetti.dto.userproblemhistory;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserProblemHistoryDto {
    private Long userId;
    private Long problemId;
    private String submittedAnswer;
    private Boolean isCorrect;
    private Integer solveTime;
}
