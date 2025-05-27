package code.rice.bowl.spaghetti.dto.ai;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * AI 서버가 돌려주는 응답 포맷
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AiServerProblemResponse {
    private String title;
    private JsonNode description;  // { question: "...", options: ["...", ...] }
    private String answer;
    private String hint;
    private int point;
    private String questionType;
}
