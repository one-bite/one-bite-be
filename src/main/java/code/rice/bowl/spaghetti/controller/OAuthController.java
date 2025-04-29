package code.rice.bowl.spaghetti.controller;

import code.rice.bowl.spaghetti.config.JwtAuthenticationFilter;
import code.rice.bowl.spaghetti.dto.JwtTokenDto;
import code.rice.bowl.spaghetti.dto.response.SimpleOkResponse;
import code.rice.bowl.spaghetti.utils.LoginProvider;
import code.rice.bowl.spaghetti.dto.request.LoginRequest;
import code.rice.bowl.spaghetti.dto.response.GoogleTokenResponse;
import code.rice.bowl.spaghetti.service.AuthService;
import code.rice.bowl.spaghetti.service.GoogleLoginService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")   // prefix
@Tag(name = "Oauth Login")
public class OAuthController {

    private final GoogleLoginService googleLoginService;
    private final AuthService authService;

    // google 로그인
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

    // access token 재발급
    @GetMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestParam String token) {
        return ResponseEntity.ok(authService.createTokenByRefresh(token));
    }

    // 로그아웃
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = request.getHeader(JwtAuthenticationFilter.AUTH_HEADER);

        // security chain 에서 필터링 되지만 혹시나 해서 추가.
        if (token == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        String userToken = token.replace(JwtAuthenticationFilter.TOKEN_PREFIX, "");
        authService.logout(userToken);

        return ResponseEntity.ok(new SimpleOkResponse("ok"));
    }

    // 사용자 인증
    @GetMapping("/verify")
    public ResponseEntity<?> verify() {
        return ResponseEntity.ok(new SimpleOkResponse("ok"));
    }
}
