package site.travellaboratory.be.presentation.user.dto.reader;

import site.travellaboratory.be.infrastructure.domains.user.entity.UserJpaEntity;

public record UserProfileResponse(
        String profileImgUrl,
        String name,
        String introduce,
        boolean isEditable
) {
    public static UserProfileResponse from(final UserJpaEntity userJpaEntity, boolean isEditable) {
        return new UserProfileResponse(userJpaEntity.getProfileImgUrl(), userJpaEntity.getNickname(), userJpaEntity.getIntroduce(), isEditable);
    }
}
