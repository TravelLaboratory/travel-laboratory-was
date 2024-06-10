package site.travellaboratory.be.controller.auth.dto;

public record UserNicknameResponse(
    Boolean available
) {
    public static UserNicknameResponse from(Boolean available) {
        return new UserNicknameResponse(
            available);
    }
}
