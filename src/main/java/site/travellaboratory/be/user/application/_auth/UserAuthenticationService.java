package site.travellaboratory.be.user.application._auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.presentation.exception.BeApplicationException;
import site.travellaboratory.be.common.presentation.error.ErrorCodes;
import site.travellaboratory.be.user.application._auth.command.LoginCommand;
import site.travellaboratory.be.user.application.port.TokenManager;
import site.travellaboratory.be.user.application.port.UserRepository;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain._auth.Token;
import site.travellaboratory.be.user.domain._auth.Tokens;
import site.travellaboratory.be.user.domain._auth.request.LoginRequest;
import site.travellaboratory.be.user.domain.enums.UserStatus;

@Service
@RequiredArgsConstructor
public class UserAuthenticationService {

    private final BCryptPasswordEncoder encoder;
    private final TokenManager tokenManager;
    private final UserRepository userRepository;

    @Transactional
    public LoginCommand login(LoginRequest request) {
        // 회원가입 여부 체크
        User user = userRepository.findByUsernameAndStatusOrderByIdDesc(
                request.username(), UserStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(
                ErrorCodes.LOGIN_USERNAME_NOT_FOUND, HttpStatus.NOT_FOUND));

        // 비밀번호 체크
        if (!encoder.matches(request.password(), user.getPassword())) {
            throw new BeApplicationException(ErrorCodes.AUTH_INVALID_PASSWORD,
                HttpStatus.UNAUTHORIZED);
        }

        Tokens tokens = tokenManager.generateTokens(user.getId());
        return LoginCommand.from(user, tokens);
    }

    public Token reIssueAccessToken(String accessToken, String refreshToken) {
        return tokenManager.reIssueAccessToken(accessToken, refreshToken);
    }
}
