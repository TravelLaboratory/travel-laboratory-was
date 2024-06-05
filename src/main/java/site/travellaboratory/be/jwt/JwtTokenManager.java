package site.travellaboratory.be.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.jwt.model.Token;

@Component
public class JwtTokenManager {

    @Value("${jwt.secret-key}")
    private @NotNull String secretKey;

    @Value("${jwt.access-token.plus-hour}")
    private @NotNull Long accessTokenPlusHour;

    @Value("${jwt.refresh-token.plus-hour}")
    private @NotNull Long refreshTokenPlusHour;

    public Token issueAccessToken(Map<String, Object> data) {
        return issueToken(data, accessTokenPlusHour);
    }

    public Token issueRefreshToken(Map<String, Object> data) {
        return issueToken(data, refreshTokenPlusHour);
    }

    public Map<String, Object> validationTokenWithThrow(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

        JwtParser parser = Jwts.parserBuilder()
            .setSigningKey(key)
            .build();

        try {
            Jws<Claims> result = parser.parseClaimsJws(token);
            return new HashMap<String, Object>(result.getBody());

        } catch (Exception e) {
            // todo : BeApplicationException (ErrorCode) 수정 후 수정할 부분 , error도 추가해야함
            if (e instanceof SignatureException) {
                // 토큰이 유효하지 않을 때
                throw new BeApplicationException(ErrorCodes.TOKEN_INVALID_TOKEN, HttpStatus.BAD_REQUEST);
            }
            else if (e instanceof ExpiredJwtException) {
                // 만료된 토큰
                throw new BeApplicationException(ErrorCodes.TOKEN_EXPIRED_TOKEN, HttpStatus.BAD_REQUEST);
            }
            else {
                // 그외 에러
                throw new BeApplicationException(ErrorCodes.TOKEN_TOKEN_EXCEPTION, HttpStatus.BAD_REQUEST);
            }
        }
    }

    private Token issueToken(Map<String, Object> data, Long tokenPlusHour) {
        LocalDateTime expiredLocalDateTime = LocalDateTime.now().plusHours(tokenPlusHour);
        // setExpiration 인자가 Date type 이기에 변환
        Date expiredAt = Date.from(expiredLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());

        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

        String jwtToken = Jwts.builder()
            .signWith(key, SignatureAlgorithm.HS256)
            .setClaims(data)
            .setExpiration(expiredAt)
            .compact();

        return Token.builder()
            .token(jwtToken)
            .expiredAt(expiredLocalDateTime)
            .build();
    }
}
