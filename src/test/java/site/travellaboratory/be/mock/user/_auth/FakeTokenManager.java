package site.travellaboratory.be.mock.user._auth;

import java.time.LocalDateTime;
import site.travellaboratory.be.user.application.port.TokenManager;
import site.travellaboratory.be.user.domain._auth.Token;
import site.travellaboratory.be.user.domain._auth.Tokens;

public class FakeTokenManager implements TokenManager {

    @Override
    public Tokens generateTokens(Long userId) {
        Token accessToken = Token.builder()
            .token("access_token")
            .expiredAt(LocalDateTime.now().plusHours(1))
            .build();

        Token refreshToken = Token.builder()
            .token("refresh_token")
            .expiredAt(LocalDateTime.now().plusHours(2))
            .build();

        return Tokens.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    @Override
    public Token reIssueAccessToken(String accessToken, String refreshToken) {
        String reIssueToken = "reIssue_token";
        LocalDateTime reIssueTokenExpiredAt = LocalDateTime.now().plusHours(1);

        return Token.builder()
            .token(reIssueToken)
            .expiredAt(reIssueTokenExpiredAt)
            .build();
    }
}
