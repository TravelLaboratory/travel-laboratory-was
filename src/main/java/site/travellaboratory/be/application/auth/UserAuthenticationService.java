package site.travellaboratory.be.application.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.infrastructure.domains.auth.jwt.helper.AuthTokenGenerator;
import site.travellaboratory.be.infrastructure.domains.user.UserRepository;
import site.travellaboratory.be.infrastructure.domains.user.entity.User;
import site.travellaboratory.be.infrastructure.domains.user.enums.UserStatus;
import site.travellaboratory.be.presentation.auth.dto.userauthentication.UserLoginRequest;
import site.travellaboratory.be.presentation.auth.dto.userauthentication.UserLoginResponse;
import site.travellaboratory.be.presentation.auth.dto.userauthentication.AccessTokenResponse;
import site.travellaboratory.be.presentation.auth.dto.userauthentication.AuthTokenResponse;

@Service
@RequiredArgsConstructor
public class UserAuthenticationService {

    private final BCryptPasswordEncoder encoder;
    private final AuthTokenGenerator authTokenGenerator;
    private final UserRepository userRepository;

    @Transactional
    public UserLoginResponse login(UserLoginRequest request) {
        // 회원가입 여부 체크
        User userEntity = userRepository.findByUsernameAndStatusOrderByIdDesc(
                request.username(), UserStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(
                ErrorCodes.AUTH_USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        // 비밀번호 체크
        if (!encoder.matches(request.password(), userEntity.getPassword())) {
            throw new BeApplicationException(ErrorCodes.AUTH_INVALID_PASSWORD,
                HttpStatus.UNAUTHORIZED);
        }

        // 토큰 생성 - accessToken, refreshToken
        Long userId = userEntity.getId();

        // profile_img_url 전송
        // 여기서 orElseThrow 에 갈일은 위에서 회원가입 여부를 체크하기 없음
        User user = userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(
                ErrorCodes.AUTH_USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        AuthTokenResponse authTokenResponse = authTokenGenerator.generateTokens(userId);

        return UserLoginResponse.from(user, authTokenResponse);
    }


    public AccessTokenResponse reIssueAccessToken(
        final String accessToken,
        final String refreshToken) {
        return authTokenGenerator.reIssueAccessToken(accessToken, refreshToken);
    }
}
