package code.rice.bowl.spaghetti.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChallengeSubmitResponse {
    private boolean correct;
    private int score;
    private boolean gameOver;
}
