package site.travellaboratory.be.user.presentation._auth.response.userregistration;

import site.travellaboratory.be.user.domain.User;

public record UserRegisterResponse(
    Long id,
    String nickname,
    String profileImgUrl
) {
    public static UserRegisterResponse from(User user) {
        return new UserRegisterResponse(
            user.getId(),
            user.getNickname(),
            user.getProfileImgUrl()
        );
    }
}
