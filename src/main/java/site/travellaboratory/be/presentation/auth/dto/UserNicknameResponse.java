package site.travellaboratory.be.presentation.auth.dto;

public record UserNicknameResponse(
    Boolean available
) {
    public static UserNicknameResponse from(Boolean available) {
        return new UserNicknameResponse(
            available);
    }
}
