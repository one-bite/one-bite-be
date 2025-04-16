package code.rice.bowl.spaghetti.dto.badge;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BadgeDto {
    @NotNull
    private String name;
    private String description;
    private String condition;
    private String imageUrl;
}