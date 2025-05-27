package code.rice.bowl.spaghetti.dto.ai;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * AI 서버가 돌려주는 문제 생성 응답 포맷
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AiProblemResponse {
    @NotNull private String title;
    @NotNull private JsonNode description;
    @NotNull private String questionType;
    @NotNull private String hint;
    @NotNull private String answer;
    @NotNull private int point;
    @NotNull
    private String commentary;
}
