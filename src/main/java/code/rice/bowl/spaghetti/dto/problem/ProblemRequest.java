package code.rice.bowl.spaghetti.dto.problem;

import code.rice.bowl.spaghetti.entity.Problem.DifficultyLevel;
import code.rice.bowl.spaghetti.entity.Problem.QuestionType;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 용도
 * - 문제 추가 요청
 * - 문제 수정 요청
 */
@Getter
@NoArgsConstructor
public class ProblemRequest {
    @NotNull
    private Long topicId;

    @NotNull
    private String title;

    private JsonNode description;

    private QuestionType questionType;

    private DifficultyLevel difficulty;

    private String hint;

    private String answer;

    private JsonNode features;

    private int score;
}