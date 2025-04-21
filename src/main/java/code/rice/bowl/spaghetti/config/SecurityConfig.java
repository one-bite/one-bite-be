package code.rice.bowl.spaghetti.config;

import code.rice.bowl.spaghetti.exception.JwtAccessDeniedHandler;
import code.rice.bowl.spaghetti.exception.JwtAuthenticationEntryPoint;
import code.rice.bowl.spaghetti.utils.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    // 로그인 아닌 상태에서 접근 가능한 도메인.
    private static final String[] LOGIN_ONCE = {
            "/oauth/google",
            "/oauth/naver"
    };

    private static final String[] PERMIT_ALL = {
            // swagger 관련 도메인
            "/api/**",
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
            "/problem/**",
            "/db/**",
            "/submit/**"
    };

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {

        http
            .csrf(AbstractHttpConfigurer::disable)
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
                                    // 그 외 모두 인증 진행.
                                    .anyRequest().authenticated())
            .exceptionHandling(ex -> ex
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                    .accessDeniedHandler(jwtAccessDeniedHandler))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtProvider);
    }
}
