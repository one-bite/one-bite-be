package code.rice.bowl.spaghetti.dto.problem;

import code.rice.bowl.spaghetti.entity.Problem.DifficultyLevel;
import code.rice.bowl.spaghetti.entity.Problem.QuestionType;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 용도
 * - 전체 조회 외 반환하는 객체
 */
@Getter
@Builder
@AllArgsConstructor
public class ProblemResponse {
    private Long problemId;
    private String title;
    private JsonNode description;
    private QuestionType questionType;
    private DifficultyLevel difficulty;
    private String hint;
    private String answer;
    private JsonNode features;
    private int score;
}