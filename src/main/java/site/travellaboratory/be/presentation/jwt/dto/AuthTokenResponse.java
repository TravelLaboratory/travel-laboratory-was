package site.travellaboratory.be.presentation.jwt.dto;

public record AuthTokenResponse(
    String accessToken,
    String refreshToken,
    String expiredAt
) {
    public static AuthTokenResponse from(String accessToken, String refreshToken, String expiredAt) {
        return new AuthTokenResponse(accessToken, refreshToken, expiredAt);
    }
}