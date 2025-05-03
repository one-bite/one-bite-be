package code.rice.bowl.spaghetti.dto.ai;

import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

/**
 * 용도
 * - ai api로 문제 생성을 요청
 * -
 */

@Getter
@NoArgsConstructor
public class AiProblemRequest {

    @NotNull
    private String Description;

    @NotNull
    private String Topics;

    @NotNull
    private String Difficulty;

    @NotNull
    private String QuestionType;

}
