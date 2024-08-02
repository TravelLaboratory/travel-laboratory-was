package site.travellaboratory.be.user.domain.request;

public record UserProfileInfoUpdateRequest(
    String nickname,
    String introduce
) {

}
