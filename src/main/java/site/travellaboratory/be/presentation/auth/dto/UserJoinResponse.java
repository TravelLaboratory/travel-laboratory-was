package site.travellaboratory.be.presentation.auth.dto;

import site.travellaboratory.be.infrastructure.user.entity.User;
import site.travellaboratory.be.infrastructure.user.enums.UserRole;

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
