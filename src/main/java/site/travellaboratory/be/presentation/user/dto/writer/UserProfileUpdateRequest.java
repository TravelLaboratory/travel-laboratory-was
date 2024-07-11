package site.travellaboratory.be.presentation.user.dto.writer;

public record UserProfileUpdateRequest(
        String nickname,
        String introduce
) {
}
