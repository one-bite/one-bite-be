package code.rice.bowl.spaghetti.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
/**
 * 일반 적인 level 객체.
 */
public class LevelDto {
    @NotNull
    String name;
    @NotNull
    int minRating;
    @NotNull
    int maxRating;
}
