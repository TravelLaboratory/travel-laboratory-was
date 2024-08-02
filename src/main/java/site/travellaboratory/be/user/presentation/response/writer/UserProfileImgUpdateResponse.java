package site.travellaboratory.be.user.presentation.response.writer;

import site.travellaboratory.be.user.domain.User;

public record UserProfileImgUpdateResponse(
    String profileImgUrl
) {
    public static UserProfileImgUpdateResponse from(User user) {
        return new UserProfileImgUpdateResponse(
            user.getProfileImgUrl()
        );
    }
}