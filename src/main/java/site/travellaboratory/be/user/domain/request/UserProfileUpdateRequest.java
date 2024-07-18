package site.travellaboratory.be.user.domain.request;

public record UserProfileUpdateRequest(
        String nickname,
        String introduce
) {
}
