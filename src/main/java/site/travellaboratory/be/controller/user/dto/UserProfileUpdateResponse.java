package site.travellaboratory.be.controller.user.dto;

public record UserProfileUpdateResponse(
        String nickname,
        String profileImgUrl,
        String introduce
) {
}
