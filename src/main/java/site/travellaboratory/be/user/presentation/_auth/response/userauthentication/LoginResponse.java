package site.travellaboratory.be.user.presentation._auth.response.userauthentication;

public record LoginResponse(
    Long userId,
    String nickname,
    String profileImgUrl
) {
    public static LoginResponse from(Long userId, String nickname, String profileImgUrl) {
        return new LoginResponse(userId, nickname, profileImgUrl);
    }
}
