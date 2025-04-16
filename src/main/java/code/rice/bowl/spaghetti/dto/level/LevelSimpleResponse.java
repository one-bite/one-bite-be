package code.rice.bowl.spaghetti.dto.level;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * level entity 에서 name, id만 담은 객체
 */
@AllArgsConstructor
@Getter
public class LevelSimpleResponse {
    private Long id;
    private String name;
}
