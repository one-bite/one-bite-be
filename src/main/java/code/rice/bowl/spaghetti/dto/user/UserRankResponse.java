package code.rice.bowl.spaghetti.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserRankResponse {
    private String name;
    private int point;
}