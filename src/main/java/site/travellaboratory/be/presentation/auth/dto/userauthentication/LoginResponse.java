package site.travellaboratory.be.presentation.auth.dto.userauthentication;

public record LoginResponse(
    Long userId,
    String nickname,
    String profileImgUrl
) {
    public static LoginResponse from(Long userId, String nickname, String profileImgUrl) {
        return new LoginResponse(userId, nickname, profileImgUrl);
    }
}
