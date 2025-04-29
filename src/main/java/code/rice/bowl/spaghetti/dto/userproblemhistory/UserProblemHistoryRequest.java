package code.rice.bowl.spaghetti.dto.userproblemhistory;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 용도
 * - 문제 풀이 기록 추가 요청
 */
@Getter
@NoArgsConstructor
public class UserProblemHistoryRequest {
    private Long userId;
    private Long problemId;
    private String submittedAnswer;
    private Boolean isCorrect;
    private Integer solveTime;
}
