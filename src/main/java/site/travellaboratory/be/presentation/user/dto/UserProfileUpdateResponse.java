package site.travellaboratory.be.presentation.user.dto;

public record UserProfileUpdateResponse(
        String nickname,
        String profileImgUrl,
        String introduce
) {
}
