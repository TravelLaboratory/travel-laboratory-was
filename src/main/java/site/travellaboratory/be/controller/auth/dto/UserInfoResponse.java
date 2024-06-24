package site.travellaboratory.be.controller.auth.dto;

import site.travellaboratory.be.domain.user.entity.User;
import site.travellaboratory.be.domain.user.entity.UserRole;

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
