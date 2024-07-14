package site.travellaboratory.be.application.auth.command;

import site.travellaboratory.be.domain.user.user.User;
import site.travellaboratory.be.presentation.auth.dto.userauthentication.AuthTokenResponse;

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