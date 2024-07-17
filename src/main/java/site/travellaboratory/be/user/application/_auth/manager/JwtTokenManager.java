package site.travellaboratory.be.user.application._auth.manager;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.user.infrastructure.jwt.helper.JwtTokenGenerator;
import site.travellaboratory.be.user.infrastructure.jwt.helper.JwtTokenParser;
import site.travellaboratory.be.user.infrastructure.jwt.helper.JwtTokenValidator;
import site.travellaboratory.be.user.presentation._auth.response.userauthentication.AccessTokenResponse;
import site.travellaboratory.be.user.presentation._auth.response.userauthentication.AuthTokenResponse;

@Component
@RequiredArgsConstructor
public class JwtTokenManager {

    private final JwtTokenGenerator jwtTokenGenerator;
    private final JwtTokenValidator jwtTokenValidator;
    private final JwtTokenParser jwtTokenParser;

    @Value("${jwt.access-token.plus-hour}")
    private Long accessTokenPlusHour;

    @Value("${jwt.refresh-token.plus-hour}")
    private Long refreshTokenPlusHour;

    public AuthTokenResponse generateTokens(Long userId) {
        String accessToken = jwtTokenGenerator.issueToken(userId, accessTokenPlusHour);
        String refreshToken = jwtTokenGenerator.issueToken(userId, refreshTokenPlusHour);

        LocalDateTime accessTokenExpiredAt = LocalDateTime.now().plusHours(accessTokenPlusHour);

        return AuthTokenResponse.from(accessToken, refreshToken, accessTokenExpiredAt.toString());
    }

    public AccessTokenResponse reIssueAccessToken(final String accessToken, final String refreshToken) {
        Long refreshTokenUserId = getRefreshTokenUserId(accessToken, refreshToken);
        String reIssueAccessToken = jwtTokenGenerator.issueToken(refreshTokenUserId, accessTokenPlusHour);

        LocalDateTime accessTokenExpiredAt = LocalDateTime.now().plusHours(accessTokenPlusHour);
        return AccessTokenResponse.from(reIssueAccessToken, accessTokenExpiredAt.toString());
    }

    private Long getRefreshTokenUserId(final String accessToken, final String refreshToken) {
        // 액세스 토큰 만료 유무 체크
        if (!(jwtTokenValidator.isValidExpiredWithThrow(accessToken))) {
            throw new BeApplicationException(ErrorCodes.TOKEN_NOT_EXPIRED_ACCESS_TOKEN,
                HttpStatus.BAD_REQUEST);
        }
        // 리프레시 토큰 검증
        jwtTokenValidator.validRefreshTokenWithThrow(refreshToken);
        return jwtTokenParser.getUserIdBy(refreshToken);
    }
}
