package code.rice.bowl.spaghetti.dto.ai;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AiProblemResponse {

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private String questionType;

    @NotNull
    private String hint;

    @NotNull
    private String answer;

    @NotNull
    private int point;

}
