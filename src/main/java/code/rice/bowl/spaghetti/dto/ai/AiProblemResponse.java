package code.rice.bowl.spaghetti.dto.ai;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

@Getter
public class AiProblemResponse {
    private String title;
    private JsonNode description;
    private String questionType;
    private String difficulty;
    private String hint;
    private String answer;
    private JsonNode features;
    private int score;
}
