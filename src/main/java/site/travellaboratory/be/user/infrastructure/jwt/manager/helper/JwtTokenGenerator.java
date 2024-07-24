package site.travellaboratory.be.user.infrastructure.jwt.manager.helper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import site.travellaboratory.be.user.domain._auth.Token;

@Component
public class JwtTokenGenerator {

    private final Key key;
    private final Long accessTokenPlusHour;
    private final Long refreshTokenPlusHour;

    public JwtTokenGenerator(
        @Value("${jwt.secret-key}") String secretKey,
        @Value("${jwt.access-token.plus-hour}") Long accessTokenPlusHour,
        @Value("${jwt.refresh-token.plus-hour}") Long refreshTokenPlusHour) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenPlusHour = accessTokenPlusHour;
        this.refreshTokenPlusHour = refreshTokenPlusHour;
    }

    public Token issueAccessToken(Long userId) {
        return getToken(userId, accessTokenPlusHour);
    }

    public Token issueRefreshToken(Long userId) {
        return getToken(userId, refreshTokenPlusHour);
    }

    private Token getToken(Long userId, Long tokenPlusHour) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);

        LocalDateTime expiredLocalDateTime = LocalDateTime.now().plusHours(tokenPlusHour);
        Date expiredAt = Date.from(expiredLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());

        String token = Jwts.builder()
            .signWith(key, SignatureAlgorithm.HS256)
            .setClaims(claims)
            .setExpiration(expiredAt)
            .compact();

        return Token.builder()
            .token(token)
            .expiredAt(expiredLocalDateTime)
            .build();
    }
}
