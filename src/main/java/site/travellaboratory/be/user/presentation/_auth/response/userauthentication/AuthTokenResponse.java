package site.travellaboratory.be.user.presentation._auth.response.userauthentication;

public record AuthTokenResponse(
    String accessToken,
    String refreshToken,
    String expiredAt
) {
    public static AuthTokenResponse from(String accessToken, String refreshToken, String expiredAt) {
        return new AuthTokenResponse(accessToken, refreshToken, expiredAt);
    }
}