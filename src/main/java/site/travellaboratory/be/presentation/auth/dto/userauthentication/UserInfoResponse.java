package site.travellaboratory.be.presentation.auth.dto.userauthentication;

import site.travellaboratory.be.infrastructure.domains.user.entity.UserJpaEntity;
import site.travellaboratory.be.domain.user.auth.enums.UserRole;

public record UserInfoResponse(
    Long userId,
    UserRole role,
    String nickname,
    String profileImgUrl
)
{
    public static UserInfoResponse from(UserJpaEntity userJpaEntity) {
        return new UserInfoResponse(
            userJpaEntity.getId(),
            userJpaEntity.getRole(),
            userJpaEntity.getNickname(),
            userJpaEntity.getProfileImgUrl()
        );
    }
}
