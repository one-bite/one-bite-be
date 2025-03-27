package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.dto.JwtTokenDto;
import code.rice.bowl.spaghetti.dto.LoginProvider;
import code.rice.bowl.spaghetti.dto.request.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final GoogleLoginService googleLoginService;

    @Transactional
    public JwtTokenDto login(LoginRequest request) {
        // 1. 사용자 정보 조회하기.
        String email = getUserEmail(request);
        return JwtTokenDto.of(email, email);
    }

    /**
     * 로그인 요청한 사용자의 이메일 정보 획득
     * @param request   요청 정보 (토큰, 토큰 제공자)
     * @return          사용자의 이메일
     */
    private String getUserEmail(LoginRequest request) {
        if (request.getProvider() == LoginProvider.GOOGLE) {
            return googleLoginService.getGoogleEmail(request.getAccessToken());
        }
        return "";
    }
}
