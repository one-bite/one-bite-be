package code.rice.bowl.spaghetti.dto.problem;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class CommentaryResponse {
    @NotNull
    private String commentary;
}
