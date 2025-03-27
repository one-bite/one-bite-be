package code.rice.bowl.spaghetti.dto.request;

import code.rice.bowl.spaghetti.dto.LoginProvider;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Data
public class LoginRequest {
    @NotNull
    String accessToken;

    @NotNull
    LoginProvider provider;
}
