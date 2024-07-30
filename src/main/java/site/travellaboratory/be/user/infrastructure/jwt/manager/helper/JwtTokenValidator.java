package site.travellaboratory.be.user.infrastructure.jwt.manager.helper;

import io.jsonwebtoken.Claims;
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

    private final JwtParser parser;

    public JwtTokenValidator(@Value("${jwt.secret-key}") String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        this.parser = Jwts.parserBuilder().setSigningKey(key).build();
    }

    public Claims parseAccessTokenClaims(String token) {
        try {
            return parser.parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw new BeApplicationException(ErrorCodes.TOKEN_EXPIRED_TOKEN, HttpStatus.UNAUTHORIZED);
        } catch (MalformedJwtException | io.jsonwebtoken.security.SecurityException e) {
            throw new BeApplicationException(ErrorCodes.TOKEN_INVALID_TOKEN, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new BeApplicationException(ErrorCodes.TOKEN_AUTHORIZATION_FAIL, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Claims parseRefreshTokenClaims(String token) {
        try {
            return parser.parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw new BeApplicationException(ErrorCodes.REFRESH_TOKEN_EXPIRED_TOKEN, HttpStatus.UNAUTHORIZED);
        } catch (MalformedJwtException | io.jsonwebtoken.security.SecurityException e) {
            throw new BeApplicationException(ErrorCodes.REFRESH_TOKEN_INVALID_REFRESH_TOKEN, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new BeApplicationException(ErrorCodes.REFRESH_TOKEN_TOKEN_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            parseAccessTokenClaims(token);
            return false;
        } catch (BeApplicationException e) {
            if (e.getErrorCodes() == ErrorCodes.TOKEN_EXPIRED_TOKEN) {
                return true;
            }
            throw e;
        }
    }
}
