package site.travellaboratory.be.user.infrastructure.jwt.helper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;

@Component
public class JwtTokenValidator {

    private final Key key;
    private final JwtParser parser;

    public JwtTokenValidator(@Value("${jwt.secret-key}") String secretKey) {
        // 생성자에서 키 생성
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.parser = Jwts.parserBuilder().setSigningKey(key).build();
    }

    public void validAccessTokenWithThrow(String token) {
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

    public void validRefreshTokenWithThrow(String token) {
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
    public boolean isValidExpiredWithThrow(String token) {
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
