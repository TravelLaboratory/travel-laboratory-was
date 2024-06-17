package site.travellaboratory.be.controller.user.dto;


import site.travellaboratory.be.domain.user.entity.User;

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
