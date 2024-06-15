package site.travellaboratory.be.controller.user.dto;

public record UserProfileUpdateResponse(
        String username,
        String nickname,
        String profileImgUrl,
        String introduce
) {
}
