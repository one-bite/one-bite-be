package code.rice.bowl.spaghetti.utils;

import code.rice.bowl.spaghetti.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    // 사용자 권한 이름.
    public static String AUTH_PREFIX = "ROLE_";
    public static String GUEST = "GUEST";
    public static String ADMIN = "ADMIN";

    // 클레임에 들어가는 이름
    private static final String ROLES = "roles";
    private static final String NEW_USER = "new_user";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-expiration}")
    private long accessTokenValidate;

    @Value("${jwt.refresh-expiration}")
    private long refreshTokenValidate;

    private final Key key;

    public JwtProvider(@Value("${jwt.secret}") String secret) {
        if (secret == null || secret.isBlank() || secret.isEmpty()) {
            throw new IllegalArgumentException("Jwt secret is null");
        }

        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 사용자 정보로 access token 발급
     * @param user  사용자 정보
     * @return      String
     */
    public String generateAccessToken(User user, boolean isAdmin) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenValidate);

        List<String> roles = new ArrayList<>();

        // admin 계정인 경우 admin 권한 부여.
        if (isAdmin) {
            roles.add(AUTH_PREFIX + ADMIN);
        }
        roles.add(AUTH_PREFIX + GUEST);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim(NEW_USER, user.isNew())
                .claim(ROLES, roles)
                .setExpiration(expiry)
                .signWith(key)
                .compact();
    }

    /**
     * 사용자 정보로 refresh token 발급
     * @param user  사용자 정보
     * @return      String
     */
    public String generateRefreshToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + refreshTokenValidate);
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(expiry)
                .signWith(key)
                .compact();
    }

    /**
     * 토큰에서 사용자 이메일 파싱
     * @param token 사용자 토큰
     * @return      user email string
     */
    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * 토큰을 사용하여 인증 토큰 생성
     * @param token 사용자 토큰
     * @return      UsernamePasswordAuthenticationToken
     */
    public Authentication getAuth(String token) {
        String userEmail = getUserName(token);
        List<String> roles = getRoles(token);

        Set<SimpleGrantedAuthority> authoritySet = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        return new UsernamePasswordAuthenticationToken(
                new org.springframework.security.core.userdetails.User(
                        userEmail, "", authoritySet),
                token,
                authoritySet
        );
    }

    /**
     * 토큰 유효성 검사 (서명 + 만료 여부 둘다 체크)
     * @param token 현재 사용자 토큰 정보
     * @return      true/false
     */
    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token); // 서명 + 만료 여부 둘 다 자동 체크

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 토큰에서 사용 이름 획득
     */
    private String getUserName(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * 토큰에서 사용자 권한 획득.
     */
    private List<String> getRoles(String token) {
        return ((List<?>) getClaims(token).get("roles")).stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    /**
     * 토큰 전체 Claim 획득
     */
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
