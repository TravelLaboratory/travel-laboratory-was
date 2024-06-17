package site.travellaboratory.be.controller.jwt.dto;

public record AccessTokenResponse(
    String accessToken,
    String expiredAt
) {
    public static AccessTokenResponse from(String accessToken, String expiredAt) {
        return new AccessTokenResponse(accessToken, expiredAt);
    }
}