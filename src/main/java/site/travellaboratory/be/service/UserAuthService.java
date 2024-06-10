package site.travellaboratory.be.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.controller.jwt.dto.AccessTokenResponse;
import site.travellaboratory.be.controller.jwt.dto.AuthTokenResponse;
import site.travellaboratory.be.controller.jwt.util.AuthTokenGenerator;
import site.travellaboratory.be.controller.auth.dto.UserJoinRequest;
import site.travellaboratory.be.controller.auth.dto.UserJoinResponse;
import site.travellaboratory.be.controller.auth.dto.UserLoginRequest;
import site.travellaboratory.be.domain.user.UserRepository;
import site.travellaboratory.be.domain.user.entity.User;
import site.travellaboratory.be.domain.user.entity.UserStatus;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final BCryptPasswordEncoder encoder;
    private final AuthTokenGenerator authTokenGenerator;
    private final UserRepository userRepository;

    @Transactional
    public UserJoinResponse join(UserJoinRequest request) {
        // 이미 가입한 유저인지 체크
        userRepository.findByUsernameAndStatusOrderByIdDesc(request.username(), UserStatus.ACTIVE)
            .ifPresent(it -> {
                throw new BeApplicationException(ErrorCodes.AUTH_DUPLICATED_USER_NAME,
                    HttpStatus.CONFLICT);
            });

        // 닉네임 중복 체크
        userRepository.findByNickname(request.nickname()).ifPresent(it -> {
            throw new BeApplicationException(ErrorCodes.AUTH_DUPLICATED_NICK_NAME,
                HttpStatus.CONFLICT);
        });

        User userEntity = userRepository.save(
            User.of(request.username(), encoder.encode(
                request.password()), request.nickname()));

        return UserJoinResponse.from(userEntity);
    }

    @Transactional
    public AuthTokenResponse login(UserLoginRequest request) {
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
        return authTokenGenerator.generateTokens(userId);
    }

    public AccessTokenResponse reIssueAccessToken(
        final String accessToken,
        final String refreshToken) {
        return authTokenGenerator.reIssueAccessToken(accessToken, refreshToken);
    }

    public String test(Long userId) {
        Optional<User> userEntity = userRepository.findById(userId);
        System.out.println("userEntity.get().getId(); = " + userEntity.get().getId());
        return "Service Ok";
    }
}
