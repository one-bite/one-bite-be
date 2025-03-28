package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.dto.JwtTokenDto;
import code.rice.bowl.spaghetti.dto.LoginProvider;
import code.rice.bowl.spaghetti.dto.request.LoginRequest;
import code.rice.bowl.spaghetti.entity.User;
import code.rice.bowl.spaghetti.exception.NotImplementedException;
import code.rice.bowl.spaghetti.repository.UserInfoRepository;
import code.rice.bowl.spaghetti.utils.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final GoogleLoginService googleLoginService;
    private final UserInfoRepository userInfoRepository;

    private final JwtProvider jwtProvider;

    /**
     * 사용자 로그인하여 JWT token 반환.
     *
     * @param request   LoginRequest
     * @return          JwtToken
     */
    @Transactional
    public JwtTokenDto login(LoginRequest request) {
        // 1. 로그인 사용자 이메일 조회하기.
        String email = getUserEmail(request);

        // 2. 사용자 데이터 가져오기.
        User user = loadOrCreateUser(email);

        // 3. JWT token 발급 하기.
        return JwtTokenDto.of(
                jwtProvider.generateAccessToken(user),
                jwtProvider.generateRefreshToken(user)
        );
    }

    /**
     * 로그인 요청한 사용자의 이메일 정보 획득
     *
     * @param request   요청 정보 (토큰, 토큰 제공자)
     * @return          사용자의 이메일
     */
    private String getUserEmail(LoginRequest request) {
        if (request.getProvider() == LoginProvider.GOOGLE) {
            return googleLoginService.getGoogleEmail(request.getAccessToken());
        }
        throw new NotImplementedException(request.getProvider() + " method isn't implemented");
    }

    /**
     * 사용자 이메일로 사용자 정보 조회하기
     * 새로운 회원인 경우 DB에 추가 후 리턴.
     *
     * @param email 사용자 이메일
     * @return      User
     */
    private User loadOrCreateUser(String email) {
        return userInfoRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(email)
                            .isNew(true)
                            .build();

                    userInfoRepository.save(newUser);

                    return newUser;
                });
    }
}
