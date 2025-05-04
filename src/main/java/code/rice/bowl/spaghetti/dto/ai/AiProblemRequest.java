package code.rice.bowl.spaghetti.dto.ai;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String description;    // 문제 설명

    @NotNull
    private List<String> topics;   // 토픽 Code 목록

    @NotNull
    private String questionType;   // 질문 유형 (multiple_choice, short_answer, true_false)

    @NotNull
    private Long userId;           // 생성 요청 사용자 ID

    @NotNull
    private Long categoryId;       // 카테고리 ID

    @NotNull
    private int count;             // 생성할 문제 수
}
