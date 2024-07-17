package site.travellaboratory.be.user.presentation.response.reader;

import site.travellaboratory.be.user.infrastructure.persistence.entity.UserEntity;

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
