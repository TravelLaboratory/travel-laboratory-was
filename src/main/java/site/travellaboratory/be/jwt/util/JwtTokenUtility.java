package site.travellaboratory.be.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;

@Component
public class JwtTokenUtility {

    private final Key key;
    private final JwtParser parser;

    public JwtTokenUtility(@Value("${jwt.secret-key}") String secretKey) {
        // 생성자에서 키 생성
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.parser = Jwts.parserBuilder()
            .setSigningKey(key)
            .build();
    }

    public String issueToken(Long userId, Long tokenPlusHour) {
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

    public Long extractUserId(String token) {
        try {
            Jws<Claims> result = parser.parseClaimsJws(token);
            HashMap<String, Object> claims = new HashMap<>(result.getBody());

            Object userId = claims.get("userId");
            parser.parseClaimsJws(token);
            return Long.parseLong(userId.toString());
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException  e) {
            throw new BeApplicationException(ErrorCodes.TOKEN_INVALID_TOKEN,
                HttpStatus.BAD_REQUEST);
        } catch (ExpiredJwtException e) {
            throw new BeApplicationException(ErrorCodes.TOKEN_EXPIRED_TOKEN,
                HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            throw new BeApplicationException(ErrorCodes.TOKEN_TOKEN_EXCEPTION,
                HttpStatus.BAD_REQUEST);
        }
    }

    // todo: 리프레시 토큰 관련 예외처리를 세분화 할 것 - 현재 accesstoken, refreshtoken 같이 하나 예외메시지를 사용 헷갈린다.
    // 리프레시 토큰을 발급받기 위해
    public boolean isExpired(String token) {
        try {
            extractUserId(token);
            return false;
        } catch (BeApplicationException e) {
            if (e.getErrorCodes() == ErrorCodes.TOKEN_EXPIRED_TOKEN) {
                return true;
            }
            throw e;
        }
    }
}
