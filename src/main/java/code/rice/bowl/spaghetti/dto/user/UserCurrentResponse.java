package code.rice.bowl.spaghetti.dto.user;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 용도
 * - 현재 사용자의 모든 데이터 조회용.
 */
@Getter
@Setter
@Builder
public class UserCurrentResponse {
    String email;
    String username;
    int rating;
    int points;
    String level;
}
