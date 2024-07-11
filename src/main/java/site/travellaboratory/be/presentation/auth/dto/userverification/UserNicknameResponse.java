package site.travellaboratory.be.presentation.auth.dto.userverification;

public record UserNicknameResponse(
    Boolean available
) {
    public static UserNicknameResponse from(Boolean available) {
        return new UserNicknameResponse(
            available);
    }
}
