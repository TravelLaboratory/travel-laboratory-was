package site.travellaboratory.be.user.presentation._auth.response.userauthentication;

public record AccessTokenResponse(
    String accessToken,
    String expiredAt
) {
    public static AccessTokenResponse from(String accessToken, String expiredAt) {
        return new AccessTokenResponse(accessToken, expiredAt);
    }
}