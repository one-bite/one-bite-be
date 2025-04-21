package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.dto.JwtTokenDto;
import code.rice.bowl.spaghetti.utils.LoginProvider;
import code.rice.bowl.spaghetti.dto.request.LoginRequest;
import code.rice.bowl.spaghetti.entity.User;
import code.rice.bowl.spaghetti.exception.InvalidRequestException;
import code.rice.bowl.spaghetti.exception.NotImplementedException;
import code.rice.bowl.spaghetti.repository.UserRepository;
import code.rice.bowl.spaghetti.utils.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userInfoRepository;
    private final UserService userService;
    private final GoogleLoginService googleLoginService;
    private final RedisService redisService;
    private final RankService rankService;

    private final JwtProvider jwtProvider;

    private final static String KEY_PREFIX = "refresh:";

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
        User user = userService.loadOrCreate(email);

        // 3. JWT token 발급 하기.
        JwtTokenDto dto = JwtTokenDto.of(
                jwtProvider.generateAccessToken(user),
                jwtProvider.generateRefreshToken(user)
        );

        // 4. 현재 사용자의 refresh token 을 redis 저장.
        redisService.setValue(KEY_PREFIX + email, dto.refreshToken());

        return dto;
    }

    /**
     * refresh token 을 이용하여 access token 을 재 발급 함.
     *
     * @param token 사용자의 refresh token
     * @return      새로운 access token 과 refresh token
     * @throws InvalidRequestException 해당 토큰이 검증 통과 못하거나, 서버에 저장한 값이 다른 경우 발생.
     */
    public JwtTokenDto createTokenByRefresh(String token) {
        // 0. 토큰 검증
        if (!jwtProvider.isTokenValid(token)) {
            throw new InvalidRequestException("check your refresh token!");
        }

        // 1. refresh token 의 이메일 조회.
        String tokenEmail = jwtProvider.getEmailFromToken(token);

        // 2. redis 에서 사용자 이메일 획득.
        String storedToken = redisService.getValue(KEY_PREFIX + tokenEmail);

        if (storedToken == null || !storedToken.equals(token)) {
            throw new InvalidRequestException("check your refresh token");
        }

        // 3. JWT token 발급 하기.
        User user = userService.getUser(tokenEmail);

        JwtTokenDto dto = JwtTokenDto.of(
                jwtProvider.generateAccessToken(user),
                jwtProvider.generateRefreshToken(user)
        );

        // 4. 현재 사용자의 refresh token 을 redis 저장.
        redisService.setValue(KEY_PREFIX + tokenEmail, dto.refreshToken());

        return dto;
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
}
