package site.travellaboratory.be.user.application._auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.user.domain._auth.UserAuth;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.application._auth.manager.JwtTokenManager;
import site.travellaboratory.be.user.infrastructure.persistence.repository.UserJpaRepository;
import site.travellaboratory.be.user.infrastructure.persistence.entity.UserEntity;
import site.travellaboratory.be.user.domain.enums.UserStatus;
import site.travellaboratory.be.user.domain._auth.request.LoginRequest;
import site.travellaboratory.be.user.application._auth.command.LoginCommand;
import site.travellaboratory.be.user.presentation._auth.response.userauthentication.AccessTokenResponse;
import site.travellaboratory.be.user.presentation._auth.response.userauthentication.AuthTokenResponse;

@Service
@RequiredArgsConstructor
public class UserAuthenticationService {

    private final BCryptPasswordEncoder encoder;
    private final JwtTokenManager jwtTokenManager;
    private final UserJpaRepository userJpaRepository;

    @Transactional
    public LoginCommand login(LoginRequest request) {
        // 회원가입 여부 체크
        UserEntity userEntity = userJpaRepository.findByUsernameAndStatusOrderByIdDesc(
                request.username(), UserStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(
                ErrorCodes.AUTH_USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        UserAuth userAuth = userEntity.toModelUserAuth();
        User user = userEntity.toModel();

        // 비밀번호 체크
        if (!encoder.matches(request.password(), userAuth.getPassword())) {
            throw new BeApplicationException(ErrorCodes.AUTH_INVALID_PASSWORD,
                HttpStatus.UNAUTHORIZED);
        }

        AuthTokenResponse authTokenResponse = jwtTokenManager.generateTokens(userAuth.getId());
        return LoginCommand.from(user, authTokenResponse);
    }

    public AccessTokenResponse reIssueAccessToken(String accessToken, String refreshToken) {
        return jwtTokenManager.reIssueAccessToken(accessToken, refreshToken);
    }
}
