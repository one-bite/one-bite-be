package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.dto.JwtTokenDto;
import code.rice.bowl.spaghetti.utils.LoginProvider;
import code.rice.bowl.spaghetti.dto.request.LoginRequest;
import code.rice.bowl.spaghetti.entity.User;
import code.rice.bowl.spaghetti.exception.InvalidRequestException;
import code.rice.bowl.spaghetti.exception.NotImplementedException;
import code.rice.bowl.spaghetti.utils.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final AdminService adminService;

    private final GoogleLoginService googleLoginService;
    private final RedisService redisService;

    private final JwtProvider jwtProvider;

    private final static String REFRESH_PREFIX = "refresh:";
    public final static String LOGOUT_PREFIX = "logout:";

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
        boolean isAdmin = adminService.isAdmin(user.getEmail());

        // 3. JWT token 발급 하기.
        JwtTokenDto dto = JwtTokenDto.of(
                jwtProvider.generateAccessToken(user, isAdmin),
                jwtProvider.generateRefreshToken(user)
        );

        // 4. 현재 사용자의 refresh token 을 redis 저장.
        redisService.setValue(REFRESH_PREFIX + email, dto.refreshToken());

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
        String tokenEmail = jwtProvider.getUserEmail(token);

        // 2. redis 에서 사용자 이메일 획득.
        String storedToken = redisService.getValue(REFRESH_PREFIX + tokenEmail);

        if (storedToken == null || !storedToken.equals(token)) {
            throw new InvalidRequestException("refresh token is expired.");
        }

        // 3. JWT token 발급 하기.
        User user = userService.getUser(tokenEmail);
        boolean isAdmin = adminService.isAdmin(user.getEmail());

        JwtTokenDto dto = JwtTokenDto.of(
                jwtProvider.generateAccessToken(user, isAdmin),
                jwtProvider.generateRefreshToken(user)
        );

        // 4. 현재 사용자의 refresh token 을 redis 저장.
        redisService.setValue(REFRESH_PREFIX + tokenEmail, dto.refreshToken());

        return dto;
    }

    public void logout(String token) {
        long exp = jwtProvider.getExpiration(token);

        // 1. 만료되지 않는 토큰에 대하여 블랙 리스트에 추가.
        redisService.setValue(LOGOUT_PREFIX + token, "logout", exp);

        // 2. 해당 사용자의 refresh 토큰 서버에서 삭제.
        String email = jwtProvider.getUserEmail(token);

        redisService.delete(REFRESH_PREFIX + email);
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
