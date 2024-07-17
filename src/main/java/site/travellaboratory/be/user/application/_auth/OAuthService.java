package site.travellaboratory.be.user.application._auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.travellaboratory.be.user.domain._auth.UserAuth;
import site.travellaboratory.be.user.domain.enums.UserStatus;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.application._auth.manager.JwtTokenManager;
import site.travellaboratory.be.user.infrastructure.persistence.repository.UserJpaRepository;
import site.travellaboratory.be.user.infrastructure.persistence.entity.UserEntity;
import site.travellaboratory.be.user.presentation._auth.response.oauth.OAuthJoinRequest;
import site.travellaboratory.be.user.presentation._auth.response.userauthentication.AuthTokenResponse;
import site.travellaboratory.be.user.application._auth.command.LoginCommand;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final UserJpaRepository userJpaRepository;
    private final JwtTokenManager jwtTokenManager;

    public LoginCommand kakaoLogin(final OAuthJoinRequest oAuthJoinRequest) {
        UserEntity userEntity = userJpaRepository.findByUsernameAndStatusOrderByIdDesc(
                oAuthJoinRequest.accountEmail(), UserStatus.ACTIVE)
            .orElseGet(() -> {
                return userJpaRepository.save(
                    UserEntity.socialOf(oAuthJoinRequest.accountEmail(), oAuthJoinRequest.profileImage(),
                        oAuthJoinRequest.profileNickname(), oAuthJoinRequest.isAgreement()));
                });

        UserAuth userAuth = userEntity.toModelUserAuth();
        User user = userEntity.toModel();

        AuthTokenResponse authTokenResponse = jwtTokenManager.generateTokens(userAuth.getId());
        return LoginCommand.from(user, authTokenResponse);
    }
}
