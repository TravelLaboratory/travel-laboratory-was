package site.travellaboratory.be.controller.oauth.dto;

import site.travellaboratory.be.controller.jwt.dto.AuthTokenResponse;

public record OAuthTokenResponse(
        String accessToken,
        String refreshToken,
        String expiredAt
) {
    public static AuthTokenResponse from(String accessToken, String refreshToken, String expiredAt) {
        return new AuthTokenResponse(accessToken, refreshToken, expiredAt);
    }
}
