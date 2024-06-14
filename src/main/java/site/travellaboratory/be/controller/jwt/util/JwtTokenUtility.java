package site.travellaboratory.be.controller.jwt.util;

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

    public Long getAccessTokenUserId(final String accessToken) {
        // token 검사
        validAccessTokenWithThrow(accessToken);
        return extractUserId(accessToken);
    }

    public Long getRefreshTokenUserId(final String accessToken, final String refreshToken) {
        // 액세스 토큰 만료 유무 체크
        if (!(isValidExpiredWithThrow(accessToken))) {
            throw new BeApplicationException(ErrorCodes.TOKEN_NOT_EXPIRED_ACCESS_TOKEN,
                HttpStatus.BAD_REQUEST);
        }
        // 리프레시 토큰 검증
        validRefreshTokenWithThrow(refreshToken);
        return extractUserId(refreshToken);
    }

    private Long extractUserId(final String token) {
        Jws<Claims> result = parser.parseClaimsJws(token);
        HashMap<String, Object> claims = new HashMap<>(result.getBody());

        Object userId = claims.get("userId");
        parser.parseClaimsJws(token);
        return Long.parseLong(userId.toString());
    }

    private void validAccessTokenWithThrow(final String token) {
        try {
            parser.parseClaimsJws(token);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException  e) {
            throw new BeApplicationException(ErrorCodes.TOKEN_INVALID_TOKEN,
                HttpStatus.BAD_REQUEST);
        } catch (ExpiredJwtException e) {
            throw new BeApplicationException(ErrorCodes.TOKEN_EXPIRED_TOKEN,
                HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            throw new BeApplicationException(ErrorCodes.TOKEN_AUTHORIZATION_FAIL,
                HttpStatus.BAD_REQUEST);
        }
    }

    private void validRefreshTokenWithThrow(final String token) {
        try {
            parser.parseClaimsJws(token);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException  e) {
            throw new BeApplicationException(ErrorCodes.TOKEN_INVALID_REFRESH_TOKEN,
                HttpStatus.BAD_REQUEST);
        } catch (ExpiredJwtException e) {
            throw new BeApplicationException(ErrorCodes.REFRESH_TOKEN_EXPIRED_TOKEN,
                HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            throw new BeApplicationException(ErrorCodes.REFRESH_TOKEN_TOKEN_EXCEPTION,
                HttpStatus.BAD_REQUEST);
        }
    }

    // 리프레시 토큰을 발급받기 위해
    private boolean isValidExpiredWithThrow(final String token) {
        try {
            validAccessTokenWithThrow(token);
            // 통과하면 아직 유효한 토큰
            return false;
        } catch (BeApplicationException e) {
            // 만료된 토큰일 경우에만!!
            if (e.getErrorCodes() == ErrorCodes.TOKEN_EXPIRED_TOKEN) {
                return true;
            }
            throw e;
        }
    }
}
