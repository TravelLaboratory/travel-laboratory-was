package site.travellaboratory.be.presentation.user.dto.reader;

import site.travellaboratory.be.infrastructure.domains.user.entity.UserEntity;

public record UserProfileResponse(
        String profileImgUrl,
        String name,
        String introduce,
        boolean isEditable
) {
    public static UserProfileResponse from(final UserEntity userEntity, boolean isEditable) {
        return new UserProfileResponse(userEntity.getProfileImgUrl(), userEntity.getNickname(), userEntity.getIntroduce(), isEditable);
    }
}
