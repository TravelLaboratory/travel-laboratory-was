package site.travellaboratory.be.presentation.auth.dto.userauthentication;

import site.travellaboratory.be.infrastructure.domains.user.entity.User;

public record UserLoginResponse(
    UserInfoResponse userInfoResponse,
    AuthTokenResponse authTokenResponse
) {
    public static UserLoginResponse from(User user, AuthTokenResponse authTokenResponse) {
        return new UserLoginResponse(
            UserInfoResponse.from(user),
            authTokenResponse
        );
    }
}
