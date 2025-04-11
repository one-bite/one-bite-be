package code.rice.bowl.spaghetti.utils;

import code.rice.bowl.spaghetti.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Component
public class JwtProvider {

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
    public String generateAccessToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenValidate);
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("new_user", user.isNew())
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
        Claims claims = getClaims(token);

        Set<SimpleGrantedAuthority> authoritySet = Collections.singleton(
                new SimpleGrantedAuthority("ROLE_USER")
        );

        return new UsernamePasswordAuthenticationToken(
                new org.springframework.security.core.userdetails.User(
                        claims.getSubject(), "", authoritySet),
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

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
