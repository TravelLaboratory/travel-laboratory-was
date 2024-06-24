package site.travellaboratory.be.controller.auth.dto;

import site.travellaboratory.be.controller.jwt.dto.AuthTokenResponse;
import site.travellaboratory.be.domain.user.entity.User;

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
