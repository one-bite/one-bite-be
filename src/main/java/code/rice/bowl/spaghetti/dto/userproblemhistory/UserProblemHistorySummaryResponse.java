package code.rice.bowl.spaghetti.dto.userproblemhistory;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserProblemHistorySummaryResponse {
    private int totalSolved;
    private int correctCount;
    private double accuracy;
    private double avgSolveTime;
}