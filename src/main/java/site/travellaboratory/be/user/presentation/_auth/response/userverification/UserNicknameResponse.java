package site.travellaboratory.be.user.presentation._auth.response.userverification;

public record UserNicknameResponse(
    Boolean isAvailable
) {
    public static UserNicknameResponse from(Boolean isAvailable) {
        return new UserNicknameResponse(
            isAvailable);
    }
}
