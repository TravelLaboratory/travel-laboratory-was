package site.travellaboratory.be.user.application._auth.command;

import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain._auth.Token;
import site.travellaboratory.be.user.domain._auth.Tokens;

public record LoginCommand(
    Long userId,
    String nickname,
    String profileImgUrl,
    Token accessToken,
    Token refreshToken
) {
    public static LoginCommand from(User user, Tokens tokens) {
        return new LoginCommand(
            user.getId(), user.getNickname(), user.getProfileImgUrl(),
            tokens.getAccessToken(), tokens.getRefreshToken());
    }
}