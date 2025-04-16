package code.rice.bowl.spaghetti.dto.user;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserSimpleResponse {
    String name;
    String level;
}
