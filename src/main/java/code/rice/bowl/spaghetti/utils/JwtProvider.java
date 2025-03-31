package code.rice.bowl.spaghetti.utils;

import code.rice.bowl.spaghetti.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
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

    public String generateRefreshToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + refreshTokenValidate);
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(expiry)
                .signWith(key)
                .compact();
    }

    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

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
