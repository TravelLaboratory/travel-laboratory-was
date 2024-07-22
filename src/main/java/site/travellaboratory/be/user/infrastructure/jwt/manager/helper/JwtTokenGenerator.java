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

@Component
public class JwtTokenGenerator {

    private final Key key;

    public JwtTokenGenerator(@Value("${jwt.secret-key}") String secretKey) {
        // 생성자에서 키 생성
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String issueToken(final Long userId, final Long tokenPlusHour) {
        // claims
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);

        // expiration
        LocalDateTime expiredLocalDateTime = LocalDateTime.now().plusHours(tokenPlusHour);
        Date expiredAt = Date.from(expiredLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
            .signWith(key, SignatureAlgorithm.HS256)
            .setClaims(claims)
            .setExpiration(expiredAt)
            .compact();
    }
}
