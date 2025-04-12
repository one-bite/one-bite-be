package code.rice.bowl.spaghetti.dto;

import code.rice.bowl.spaghetti.entity.Problem.DifficultyLevel;
import code.rice.bowl.spaghetti.entity.Problem.QuestionType;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProblemDto {
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