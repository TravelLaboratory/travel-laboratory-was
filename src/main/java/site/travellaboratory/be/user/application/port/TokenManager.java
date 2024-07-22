package site.travellaboratory.be.user.application.port;

import site.travellaboratory.be.user.domain._auth.AccessToken;
import site.travellaboratory.be.user.domain._auth.AuthTokens;

public interface TokenManager {
    AuthTokens generateTokens(Long userId);
    AccessToken reIssueAccessToken(String accessToken, String refreshToken);
}
