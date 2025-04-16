package code.rice.bowl.spaghetti.dto.level;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LevelResponse {
    Long id;
    String name;
    int minRating;
    int maxRating;
}
