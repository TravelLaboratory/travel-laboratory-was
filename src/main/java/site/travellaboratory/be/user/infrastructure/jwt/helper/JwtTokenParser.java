package site.travellaboratory.be.user.infrastructure.jwt.helper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenParser {

    private final Key key;
    private final JwtParser parser;

    public JwtTokenParser(@Value("${jwt.secret-key}") String secretKey) {
        // 생성자에서 키 생성
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.parser = Jwts.parserBuilder()
            .setSigningKey(key)
            .build();
    }

    public Long getUserIdBy(final String token) {
        Jws<Claims> result = parser.parseClaimsJws(token);
        HashMap<String, Object> claims = new HashMap<>(result.getBody());

        Object userId = claims.get("userId");
        parser.parseClaimsJws(token);
        return Long.parseLong(userId.toString());
    }
}
