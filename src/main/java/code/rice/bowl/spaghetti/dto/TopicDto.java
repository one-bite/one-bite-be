package code.rice.bowl.spaghetti.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TopicDto {
    @NotNull
    private String name;

    private String description;

    private int total;
}
