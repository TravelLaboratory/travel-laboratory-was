package site.travellaboratory.be.controller.jwt.dto;

public record AuthTokenResponse(
    String accessToken,
    String refreshToken
) {
    public static AuthTokenResponse from(String accessToken, String refreshToken) {
        return new AuthTokenResponse(accessToken, refreshToken);
    }
}