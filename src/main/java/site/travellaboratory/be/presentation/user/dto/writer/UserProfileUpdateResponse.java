package site.travellaboratory.be.presentation.user.dto.writer;

public record UserProfileUpdateResponse(
        String nickname,
        String profileImgUrl,
        String introduce
) {
}
