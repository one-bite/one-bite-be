package code.rice.bowl.spaghetti.dto.ai;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Convert;
import code.rice.bowl.spaghetti.utils.JsonNodeConverter;
import java.util.List;

/**
 * AI 문제 생성 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class AiProblemRequest {

    @NotNull
    private Long parentProblemId;  // 파생 기준이 되는 부모 문제 ID

    @NotNull
    @Convert(converter = JsonNodeConverter.class)
    private JsonNode description;  // 문제 설명 (JSON: { question: "...", options: ["...", ...] })

    @NotNull
    private List<String> topics;   // 토픽 Code 목록

    @NotNull
    private String questionType;   // 질문 유형 (multiple_choice, short_answer, true_false)
}
