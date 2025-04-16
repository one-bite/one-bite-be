package code.rice.bowl.spaghetti.dto.problem;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProblemRequest {
    @NotNull
    private String title;
    @NotNull
    private JsonNode description;
    @NotNull
    private String answer;
    @NotNull
    private int score;
    @NotNull
    private String questionType;
    @NotNull
    private String difficulty;
    private String hint;
    private int topic_id;
}
