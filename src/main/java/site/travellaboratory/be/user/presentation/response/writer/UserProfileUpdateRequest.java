package site.travellaboratory.be.user.presentation.response.writer;

public record UserProfileUpdateRequest(
        String nickname,
        String introduce
) {
}
