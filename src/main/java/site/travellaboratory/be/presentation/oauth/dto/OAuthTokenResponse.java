package site.travellaboratory.be.presentation.oauth.dto;

import site.travellaboratory.be.presentation.jwt.dto.AuthTokenResponse;

public record OAuthTokenResponse(
        String accessToken,
        String refreshToken,
        String expiredAt
) {
    public static AuthTokenResponse from(String accessToken, String refreshToken, String expiredAt) {
        return new AuthTokenResponse(accessToken, refreshToken, expiredAt);
    }
}
