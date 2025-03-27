package code.rice.bowl.spaghetti.controller;

import code.rice.bowl.spaghetti.dto.JwtTokenDto;
import code.rice.bowl.spaghetti.dto.LoginProvider;
import code.rice.bowl.spaghetti.dto.request.LoginRequest;
import code.rice.bowl.spaghetti.dto.response.GoogleTokenResponse;
import code.rice.bowl.spaghetti.service.AuthService;
import code.rice.bowl.spaghetti.service.GoogleLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")   // prefix
public class OAuthController {

    private final GoogleLoginService googleLoginService;
    private final AuthService authService;

    @GetMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestParam String token) {
        // 1. access token 발급.
        GoogleTokenResponse accessToken = googleLoginService.getAccessToken(token);

        LoginRequest request = new LoginRequest(accessToken.getAccess_token(),
                LoginProvider.GOOGLE);

        // 2. 로그인 로직 처리
        JwtTokenDto jwt = authService.login(request);

        // 3. 반환
        return ResponseEntity.ok(jwt);
    }
}
