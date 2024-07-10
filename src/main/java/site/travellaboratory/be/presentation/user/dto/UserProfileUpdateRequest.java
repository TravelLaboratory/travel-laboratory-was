package site.travellaboratory.be.presentation.user.dto;

public record UserProfileUpdateRequest(
        String nickname,
        String introduce
) {
}
