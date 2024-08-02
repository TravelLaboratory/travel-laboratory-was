package site.travellaboratory.be.user.presentation.response.writer;

import site.travellaboratory.be.user.domain.User;

public record UserProfileInfoUpdateResponse(
        String nickname,
        String introduce
) {
    public static UserProfileInfoUpdateResponse from(User user) {
        return new UserProfileInfoUpdateResponse(
            user.getNickname(),
            user.getIntroduce()
        );
    }
}
