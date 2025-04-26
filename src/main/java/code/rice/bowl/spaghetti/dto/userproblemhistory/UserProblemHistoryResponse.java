package code.rice.bowl.spaghetti.dto.userproblemhistory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 용도
 * - 문제 풀이 기록 조회 반환 객체
 */
@Getter
@Builder
@AllArgsConstructor
public class UserProblemHistoryResponse {
    private Long historyId;
    private Long userId;
    private Long problemId;
    private String submittedAnswer;
    private Boolean isCorrect;
    private Integer solveTime;
    private LocalDateTime submittedAt;
}
