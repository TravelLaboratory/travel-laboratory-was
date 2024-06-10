package site.travellaboratory.be.controller.jwt.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import site.travellaboratory.be.controller.jwt.dto.AccessTokenResponse;
import site.travellaboratory.be.controller.jwt.dto.AuthTokenResponse;

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
        return AuthTokenResponse.from(accessToken, refreshToken);
    }

    public AccessTokenResponse reIssueAccessToken(final String accessToken, final String refreshToken) {
        Long refreshTokenUserId = jwtTokenUtility.getRefreshTokenUserId(accessToken, refreshToken);
        String reIssueAccessToken = jwtTokenUtility.issueToken(refreshTokenUserId, accessTokenPlusHour);
        return AccessTokenResponse.from(reIssueAccessToken);
    }
}
