package site.travellaboratory.be.presentation.auth.dto.userauthentication;

import site.travellaboratory.be.infrastructure.user.entity.User;
import site.travellaboratory.be.infrastructure.user.enums.UserRole;

public record UserInfoResponse(
    Long userId,
    UserRole role,
    String nickname,
    String profileImgUrl
)
{
    public static UserInfoResponse from(User user) {
        return new UserInfoResponse(
            user.getId(),
            user.getRole(),
            user.getNickname(),
            user.getProfileImgUrl()
        );
    }
}
