package site.travellaboratory.be.presentation.user.dto;

import site.travellaboratory.be.infrastructure.user.entity.User;

public record UserProfileResponse(
        String profileImgUrl,
        String name,
        String introduce,
        boolean isEditable
) {
    public static UserProfileResponse from(final User user, boolean isEditable) {
        return new UserProfileResponse(user.getProfileImgUrl(), user.getNickname(), user.getIntroduce(), isEditable);
    }
}
