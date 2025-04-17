package code.rice.bowl.spaghetti.dto.level;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 용도
 * -
 */
@Getter
@Setter
@Builder
public class LevelRequest {
    @NotNull
    String name;
    @NotNull
    int minRating;
    @NotNull
    int maxRating;
}
