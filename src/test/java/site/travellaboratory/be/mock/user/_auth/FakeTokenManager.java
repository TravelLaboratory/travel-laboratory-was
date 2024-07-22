package site.travellaboratory.be.mock.user._auth;

import java.time.LocalDateTime;
import site.travellaboratory.be.user.application.port.TokenManager;
import site.travellaboratory.be.user.domain._auth.AccessToken;
import site.travellaboratory.be.user.domain._auth.AuthTokens;

public class FakeTokenManager implements TokenManager {

    @Override
    public AuthTokens generateTokens(Long userId) {
        String accessToken = "access_token";
        String refreshToken = "refresh_token";
        String expiredAt = LocalDateTime.now().plusHours(1).toString();

        return AuthTokens.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .expiredAt(expiredAt)
            .build();
    }

    @Override
    public AccessToken reIssueAccessToken(String accessToken, String refreshToken) {
        String reIssueToken = "reIssue_token";
        String reIssueTokenExpiredAt = LocalDateTime.now().plusHours(1).toString();

        return AccessToken.builder()
            .accessToken(reIssueToken)
            .expiredAt(reIssueTokenExpiredAt)
            .build();
    }
}
