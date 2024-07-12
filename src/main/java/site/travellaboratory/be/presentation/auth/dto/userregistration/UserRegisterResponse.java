package site.travellaboratory.be.presentation.auth.dto.userregistration;

import site.travellaboratory.be.domain.user.User;

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
