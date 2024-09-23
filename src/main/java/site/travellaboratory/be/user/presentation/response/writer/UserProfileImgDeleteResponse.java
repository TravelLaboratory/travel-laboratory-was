package site.travellaboratory.be.user.presentation.response.writer;

import site.travellaboratory.be.user.domain.User;

public record UserProfileImgDeleteResponse(
    String profileImgUrl
) {
    public static UserProfileImgDeleteResponse from(User user) {
        return new UserProfileImgDeleteResponse(
            user.getProfileImgUrl()
        );
    }
}