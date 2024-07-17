package site.travellaboratory.be.application.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.travellaboratory.be.domain.user.auth.UserAuth;
import site.travellaboratory.be.domain.user.enums.UserStatus;
import site.travellaboratory.be.domain.user.user.User;
import site.travellaboratory.be.application.auth.manager.JwtTokenManager;
import site.travellaboratory.be.infrastructure.domains.user.UserJpaRepository;
import site.travellaboratory.be.infrastructure.domains.user.entity.UserEntity;
import site.travellaboratory.be.presentation.auth.dto.oauth.OAuthJoinRequest;
import site.travellaboratory.be.presentation.auth.dto.userauthentication.AuthTokenResponse;
import site.travellaboratory.be.application.auth.command.LoginCommand;

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
