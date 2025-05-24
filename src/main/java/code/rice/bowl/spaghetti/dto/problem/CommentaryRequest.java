package code.rice.bowl.spaghetti.dto.problem;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class CommentaryRequest {
    @NotNull
    private Long id;                // 0 이면 DB 저장 생략

    @NotNull
    private JsonNode description;   // { question: "...", options: [...] }
}
