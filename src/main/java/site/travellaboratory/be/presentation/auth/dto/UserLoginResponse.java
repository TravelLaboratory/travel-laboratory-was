package site.travellaboratory.be.presentation.auth.dto;

import site.travellaboratory.be.presentation.jwt.dto.AuthTokenResponse;
import site.travellaboratory.be.infrastructure.user.entity.User;

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
