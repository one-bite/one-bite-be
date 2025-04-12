package code.rice.bowl.spaghetti.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 일반 적인 level 객체.
 */
@Getter
@Setter
@Builder
public class LevelDto {
    @NotNull
    String name;
    @NotNull
    int minRating;
    @NotNull
    int maxRating;
}
