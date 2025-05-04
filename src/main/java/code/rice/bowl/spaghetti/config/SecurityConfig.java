package code.rice.bowl.spaghetti.config;

import code.rice.bowl.spaghetti.exception.JwtAccessDeniedHandler;
import code.rice.bowl.spaghetti.exception.JwtAuthenticationEntryPoint;
import code.rice.bowl.spaghetti.exception.NotFoundException;
import code.rice.bowl.spaghetti.service.RedisService;
import code.rice.bowl.spaghetti.utils.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.rmi.AccessException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final NotFoundApiFilter notFoundApiFilter;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    // 로그인 아닌 상태에서 접근 가능한 도메인.
    private static final String[] LOGIN_ONCE = {
            "/oauth/google",
            "/oauth/naver"
    };

    private static final String[] ALLOW_ADMIN = {
            "/db/**",
    };

    private static final String[] ALLOW_GUEST = {
            "/submit/**",
            "/problem/**",
            "/oauth/logout",
            "/oauth/verify",
            "/users/**"
    };

    private static final String[] PERMIT_ALL = {
            // swagger 관련
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/configuration/**",
            "/swagger-resources/**",
            "/webjars/**",
            // 실제 무조건 허용하는 도메인
            "/oauth/refresh",
            "/",
            "/test",
            // 임시 전체 허용.
            "/submit/**",
            "/problem/**",
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())    // disable cors
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .sessionManagement(config ->
                            config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authManager ->
                            authManager
                                    // 로그인한 유저가 다시 접근하는 것을 예방.
                                    .requestMatchers(LOGIN_ONCE).anonymous()
                                    // 로그인 검증이 필요 없는 API 허용
                                    .requestMatchers(PERMIT_ALL).permitAll()
                                    // 관리자 경우에만 접근 가능.
                                    .requestMatchers(ALLOW_ADMIN).hasRole(JwtProvider.ADMIN)
                                    // 일반 사용자 인 경우에만 접근 가능.
                                    .requestMatchers(ALLOW_GUEST).hasRole(JwtProvider.GUEST)
                                    // 그 외 요청에 대하여 허용 -> NotFoundApiFilter에서 처리.
                                    .anyRequest().permitAll()
                                   )
            .exceptionHandling(ex -> ex
                    // 인증이 안된 사용자가 인증을 필요하는 API 접근 시 발생.
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                    // 인증되 었지만 접근 권한이 없는 경우 발생.
                    .accessDeniedHandler(jwtAccessDeniedHandler))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(notFoundApiFilter, JwtAuthenticationFilter.class);

        return http.build();
    }

}
