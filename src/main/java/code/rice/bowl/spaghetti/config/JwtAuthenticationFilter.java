package code.rice.bowl.spaghetti.config;

import code.rice.bowl.spaghetti.service.AuthService;
import code.rice.bowl.spaghetti.service.RedisService;
import code.rice.bowl.spaghetti.utils.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final RedisService redisService;

    public final static String AUTH_HEADER = "Authorization";
    public final static String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 1. 요청 헤더으로 부터 토큰 파싱.
        String authorizationHeader = request.getHeader(AUTH_HEADER);
        String token = getAccessToken(authorizationHeader);

        // 2. 해당 토큰이 유효한지 설정.
        if (jwtProvider.isTokenValid(token)) {
            Authentication auth = jwtProvider.getAuth(token);

            // 3. 로그아웃 된 사용자의 토큰 인지 확인.
            if (redisService.hasKey(AuthService.LOGOUT_PREFIX + token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"status\": \"401\", \"message\": \"logout token. Please Login Again\"}");

                return;
            }

            // 사용자 인증 정보 설정.
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }

    private String getAccessToken(String token) {
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
