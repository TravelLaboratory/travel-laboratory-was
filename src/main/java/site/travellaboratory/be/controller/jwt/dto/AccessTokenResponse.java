package site.travellaboratory.be.controller.jwt.dto;

public record AccessTokenResponse(
    String accessToken
) {
    public static AccessTokenResponse from(String accessToken) {
        return new AccessTokenResponse(accessToken);
    }
}