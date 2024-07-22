package site.travellaboratory.be.user.presentation._auth.response.userauthentication;

import site.travellaboratory.be.user.domain._auth.AccessToken;

public record AuthTokensResponse(
    String accessToken,
    String refreshToken,
    String expiredAt
) {
    public static AuthTokensResponse from(AccessToken accessToken, String refreshToken) {
        return new AuthTokensResponse(
            accessToken.getAccessToken(),
            refreshToken,
            accessToken.getExpiredAt());
    }
}