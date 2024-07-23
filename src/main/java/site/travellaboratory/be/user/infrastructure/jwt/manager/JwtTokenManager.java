package site.travellaboratory.be.user.infrastructure.jwt.manager;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.user.application.port.TokenManager;
import site.travellaboratory.be.user.domain._auth.AccessToken;
import site.travellaboratory.be.user.domain._auth.AuthTokens;
import site.travellaboratory.be.user.infrastructure.jwt.manager.helper.JwtTokenGenerator;
import site.travellaboratory.be.user.infrastructure.jwt.manager.helper.JwtTokenParser;
import site.travellaboratory.be.user.infrastructure.jwt.manager.helper.JwtTokenValidator;

@Component
public class JwtTokenManager implements TokenManager {

    private final JwtTokenGenerator jwtTokenGenerator;
    private final JwtTokenValidator jwtTokenValidator;
    private final JwtTokenParser jwtTokenParser;
    private final Long accessTokenPlusHour;
    private final Long refreshTokenPlusHour;

    public JwtTokenManager(
        JwtTokenGenerator jwtTokenGenerator,
        JwtTokenValidator jwtTokenValidator,
        JwtTokenParser jwtTokenParser,
        @Value("${jwt.access-token.plus-hour}") Long accessTokenPlusHour,
        @Value("${jwt.refresh-token.plus-hour}") Long refreshTokenPlusHour) {
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.jwtTokenValidator = jwtTokenValidator;
        this.jwtTokenParser = jwtTokenParser;
        this.accessTokenPlusHour = accessTokenPlusHour;
        this.refreshTokenPlusHour = refreshTokenPlusHour;
    }

    @Override
    public AuthTokens generateTokens(Long userId) {
        String accessToken = jwtTokenGenerator.issueToken(userId, accessTokenPlusHour);
        String refreshToken = jwtTokenGenerator.issueToken(userId, refreshTokenPlusHour);

        LocalDateTime accessTokenExpiredAt = LocalDateTime.now().plusHours(accessTokenPlusHour);

        return AuthTokens.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .expiredAt(accessTokenExpiredAt.toString())
            .build();
    }

    @Override
    public AccessToken reIssueAccessToken(final String accessToken, final String refreshToken) {
        Long refreshTokenUserId = getRefreshTokenUserId(accessToken, refreshToken);
        String reIssueToken = jwtTokenGenerator.issueToken(refreshTokenUserId, accessTokenPlusHour);
        LocalDateTime reIssueTokenExpiredAt = LocalDateTime.now().plusHours(accessTokenPlusHour);

        return AccessToken.builder()
            .accessToken(reIssueToken)
            .expiredAt(reIssueTokenExpiredAt.toString())
            .build();
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
