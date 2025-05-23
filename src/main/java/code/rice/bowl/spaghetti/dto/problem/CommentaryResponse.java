package code.rice.bowl.spaghetti.dto.problem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentaryResponse {
    private Long problemId;
    private String commentary;
}
