package site.travellaboratory.be.user.application.port;

import site.travellaboratory.be.user.domain._auth.Token;
import site.travellaboratory.be.user.domain._auth.Tokens;

public interface TokenManager {
    Tokens generateTokens(Long userId);
    Token reIssueAccessToken(String accessToken, String refreshToken);
}
