package site.travellaboratory.be.application.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.domain.user.auth.UserAuth;
import site.travellaboratory.be.domain.user.user.User;
import site.travellaboratory.be.application.auth.manager.JwtTokenManager;
import site.travellaboratory.be.infrastructure.domains.user.UserJpaRepository;
import site.travellaboratory.be.infrastructure.domains.user.entity.UserEntity;
import site.travellaboratory.be.domain.user.enums.UserStatus;
import site.travellaboratory.be.presentation.auth.dto.userauthentication.LoginRequest;
import site.travellaboratory.be.application.auth.command.LoginCommand;
import site.travellaboratory.be.presentation.auth.dto.userauthentication.AccessTokenResponse;
import site.travellaboratory.be.presentation.auth.dto.userauthentication.AuthTokenResponse;

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
