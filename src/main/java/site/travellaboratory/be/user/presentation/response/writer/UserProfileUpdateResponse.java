package site.travellaboratory.be.user.presentation.response.writer;

public record UserProfileUpdateResponse(
        String nickname,
        String profileImgUrl,
        String introduce
) {
}
