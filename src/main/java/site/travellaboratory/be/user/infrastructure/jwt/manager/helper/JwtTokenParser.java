package site.travellaboratory.be.user.infrastructure.jwt.manager.helper;

import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.error.ErrorCodes;

@Component
public class JwtTokenParser {

    private final JwtTokenValidator jwtTokenValidator;

    public JwtTokenParser(JwtTokenValidator jwtTokenValidator) {
        this.jwtTokenValidator = jwtTokenValidator;
    }

    public Long getRefreshTokenUserId(String accessToken, String refreshToken) {
        if (!jwtTokenValidator.isTokenExpired(accessToken)) {
            throw new BeApplicationException(ErrorCodes.TOKEN_NOT_EXPIRED_ACCESS_TOKEN,
                HttpStatus.BAD_REQUEST);
        }
        Claims claims = jwtTokenValidator.parseRefreshTokenClaims(refreshToken);
        return Long.parseLong(claims.get("userId").toString());
    }

//    public Long getUserIdBy(String token) {
//        Claims claims = jwtTokenValidator.parseAccessTokenClaims(token);
//        return Long.parseLong(claims.get("userId").toString());
//    }
}
