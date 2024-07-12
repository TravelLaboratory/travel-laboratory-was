package site.travellaboratory.be.presentation.auth.dto.userauthentication;

import site.travellaboratory.be.infrastructure.domains.user.entity.UserJpaEntity;

public record UserLoginResponse(
    UserInfoResponse userInfoResponse,
    AuthTokenResponse authTokenResponse
) {
    public static UserLoginResponse from(UserJpaEntity userJpaEntity, AuthTokenResponse authTokenResponse) {
        return new UserLoginResponse(
            UserInfoResponse.from(userJpaEntity),
            authTokenResponse
        );
    }
}
