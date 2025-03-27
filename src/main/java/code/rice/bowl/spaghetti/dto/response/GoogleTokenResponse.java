package code.rice.bowl.spaghetti.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 구글 auth code 로 access token 을 발급 받을 때 받게 되는 dto
 */
@Getter
@NoArgsConstructor
public class GoogleTokenResponse {

    String access_token;
    Integer expires_in;
    String scope;
    String token_type;
    String id_token;
}
