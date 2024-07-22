package site.travellaboratory.be.user.presentation._auth.response.userauthentication;

import site.travellaboratory.be.user.domain._auth.AccessToken;

public record AccessTokenResponse(
    String accessToken,
    String expiredAt
) {
    public static AccessTokenResponse from(AccessToken token) {
        return new AccessTokenResponse(token.getAccessToken(), token.getExpiredAt());
    }
}