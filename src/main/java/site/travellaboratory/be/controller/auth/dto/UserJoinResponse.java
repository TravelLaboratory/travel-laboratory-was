package site.travellaboratory.be.controller.auth.dto;

import site.travellaboratory.be.domain.user.entity.User;
import site.travellaboratory.be.domain.user.entity.UserRole;

public record UserJoinResponse(
    Long id,
    String username,
    UserRole role,
    String nickname,
    String profileImgUrl
) {
    public static UserJoinResponse from(User user) {
        return new UserJoinResponse(
            user.getId(),
            user.getUsername(),
            user.getRole(),
            user.getNickname(),
            user.getProfileImgUrl()
        );
    }
}
