package site.travellaboratory.be.user.application._auth.command;

import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.presentation._auth.response.userauthentication.AuthTokenResponse;

public record LoginCommand(
    Long userId,
    String nickname,
    String profileImgUrl,
    String accessToken,
    String refreshToken,
    String expiredAt
) {
    public static LoginCommand from(User user, AuthTokenResponse token) {
        return new LoginCommand(
            user.getId(), user.getNickname(), user.getProfileImgUrl(),
            token.accessToken(), token.refreshToken(), token.expiredAt());
    }
}