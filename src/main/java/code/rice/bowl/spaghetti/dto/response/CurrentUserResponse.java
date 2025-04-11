package code.rice.bowl.spaghetti.dto.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CurrentUserResponse {
    String email;
    String username;
    int rating;
    int points;
    String level;
}
