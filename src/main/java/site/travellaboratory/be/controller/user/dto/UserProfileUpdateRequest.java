package site.travellaboratory.be.controller.user.dto;

public record UserProfileUpdateRequest(
        String username,
        String nickname,
        String introduce
) {
}
