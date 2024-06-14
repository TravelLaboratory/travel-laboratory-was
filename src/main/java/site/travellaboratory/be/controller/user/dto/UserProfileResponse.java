package site.travellaboratory.be.controller.user.dto;


import site.travellaboratory.be.domain.user.entity.User;

public record UserProfileResponse (
        String profileImgUrl,
        String name,
        String introduce
){
    public static UserProfileResponse from(final User user) {
        return new UserProfileResponse(user.getProfileImgUrl(), user.getNickname(), user.getIntroduce());
    }
}