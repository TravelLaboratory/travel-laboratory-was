package site.travellaboratory.be.user.presentation._auth.response.userverification;

public record UserNicknameResponse(
    Boolean available
) {
    public static UserNicknameResponse from(Boolean available) {
        return new UserNicknameResponse(
            available);
    }
}
