package site.travellaboratory.be.user.application._auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.travellaboratory.be.user.application._auth.command.LoginCommand;
import site.travellaboratory.be.user.infrastructure.jwt.manager.JwtTokenManager;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain._auth.AuthTokens;
import site.travellaboratory.be.user.domain._auth.request.OAuthJoinRequest;
import site.travellaboratory.be.user.domain.enums.UserStatus;
import site.travellaboratory.be.user.infrastructure.persistence.entity.UserEntity;
import site.travellaboratory.be.user.infrastructure.persistence.repository.UserJpaRepository;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final UserJpaRepository userJpaRepository;
    private final JwtTokenManager jwtTokenManager;

    public LoginCommand kakaoLogin(final OAuthJoinRequest oAuthJoinRequest) {
        User user = userJpaRepository.findByUsernameAndStatusOrderByIdDesc(
                oAuthJoinRequest.accountEmail(), UserStatus.ACTIVE)
            .orElseGet(() -> {
                return userJpaRepository.save(
                    UserEntity.socialOf(oAuthJoinRequest.accountEmail(), oAuthJoinRequest.profileImage(),
                        oAuthJoinRequest.profileNickname(), oAuthJoinRequest.isAgreement()));
                }).toModel();

        AuthTokens authTokens = jwtTokenManager.generateTokens(user.getId());
        return LoginCommand.from(user, authTokens);
    }
}
