package code.rice.bowl.spaghetti.dto.response;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Data
public class GoogleUserInfoResponse {
    String sub;
    String name;
    String given_name;
    String family_name;
    String picture;
    String email;
    Boolean email_verified;
}
