package site.travellaboratory.be.infrastructure.domains.auth.jwt.helper;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import site.travellaboratory.be.presentation.auth.dto.userauthentication.AccessTokenResponse;
import site.travellaboratory.be.presentation.auth.dto.userauthentication.AuthTokenResponse;

@Component
@RequiredArgsConstructor
public class AuthTokenGenerator {

    private final JwtTokenUtility jwtTokenUtility;

    @Value("${jwt.access-token.plus-hour}")
    private Long accessTokenPlusHour;

    @Value("${jwt.refresh-token.plus-hour}")
    private Long refreshTokenPlusHour;

    public AuthTokenResponse generateTokens(Long userId) {
        String accessToken = jwtTokenUtility.issueToken(userId, accessTokenPlusHour);
        String refreshToken = jwtTokenUtility.issueToken(userId, refreshTokenPlusHour);

        // accessToken 유효기간도 주기로 변경
        LocalDateTime accessTokenExpiredAt = jwtTokenUtility.getAccessTokenExpiredAt(accessTokenPlusHour);

        return AuthTokenResponse.from(accessToken, refreshToken, accessTokenExpiredAt.toString());
    }

    public AccessTokenResponse reIssueAccessToken(final String accessToken, final String refreshToken) {
        Long refreshTokenUserId = jwtTokenUtility.getRefreshTokenUserId(accessToken, refreshToken);
        String reIssueAccessToken = jwtTokenUtility.issueToken(refreshTokenUserId, accessTokenPlusHour);

        // accessToken 유효기간도 주기로 변경
        LocalDateTime accessTokenExpiredAt = jwtTokenUtility.getAccessTokenExpiredAt(accessTokenPlusHour);
        return AccessTokenResponse.from(reIssueAccessToken, accessTokenExpiredAt.toString());
    }
}
