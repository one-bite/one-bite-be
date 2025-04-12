package code.rice.bowl.spaghetti.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubmitResponse {
    private boolean correct;
    private int score;
}
