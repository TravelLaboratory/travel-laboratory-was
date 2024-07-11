package site.travellaboratory.be.presentation.auth.dto.userregistration;

import site.travellaboratory.be.infrastructure.user.entity.User;
import site.travellaboratory.be.infrastructure.user.enums.UserRole;

public record UserRegisterResponse(
    Long id,
    String username,
    UserRole role,
    String nickname,
    String profileImgUrl
) {
    public static UserRegisterResponse from(User user) {
        return new UserRegisterResponse(
            user.getId(),
            user.getUsername(),
            user.getRole(),
            user.getNickname(),
            user.getProfileImgUrl()
        );
    }
}
