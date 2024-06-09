package site.travellaboratory.be.user.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.jwt.dto.AccessTokenResponse;
import site.travellaboratory.be.jwt.dto.AuthTokenResponse;
import site.travellaboratory.be.jwt.util.AuthTokenGenerator;
import site.travellaboratory.be.user.controller.dto.UserJoinRequest;
import site.travellaboratory.be.user.controller.dto.UserJoinResponse;
import site.travellaboratory.be.user.controller.dto.UserLoginRequest;
import site.travellaboratory.be.user.repository.UserAuthRepository;
import site.travellaboratory.be.user.repository.entity.UserEntity;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final BCryptPasswordEncoder encoder;
    private final AuthTokenGenerator authTokenGenerator;
    private final UserAuthRepository userAuthRepository;

    @Transactional
    public UserJoinResponse join(UserJoinRequest request) {
        // 이미 가입한 유저인지 체크
        userAuthRepository.findByUserNameAndDeleteAtOrderByIdDesc(request.userName(), null)
            .ifPresent(it -> {
                throw new BeApplicationException(ErrorCodes.AUTH_DUPLICATED_USER_NAME,
                    HttpStatus.CONFLICT);
            });

        // 닉네임 중복 체크
        userAuthRepository.findByNickName(request.nickName()).ifPresent(it -> {
            throw new BeApplicationException(ErrorCodes.AUTH_DUPLICATED_NICK_NAME,
                HttpStatus.CONFLICT);
        });

        UserEntity userEntity = userAuthRepository.save(
            UserEntity.of(request.userName(), encoder.encode(
                request.password()), request.nickName()));

        return UserJoinResponse.from(userEntity);
    }

    @Transactional
    public AuthTokenResponse login(UserLoginRequest request) {
        // 회원가입 여부 체크
        UserEntity userEntity = userAuthRepository.findByUserNameAndDeleteAtOrderByIdDesc(
                request.userName(), null)
            .orElseThrow(() -> new BeApplicationException(
                ErrorCodes.AUTH_USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        // 비밀번호 체크
        if (!encoder.matches(request.password(), userEntity.getPassword())) {
            throw new BeApplicationException(ErrorCodes.AUTH_INVALID_PASSWORD,
                HttpStatus.UNAUTHORIZED);
        }

        // 토큰 생성
        Long userId = userEntity.getId();
        AuthTokenResponse authTokenResponse = authTokenGenerator.generateTokens(userId);

        // 리프레시 토큰은 db에 저장
        userAuthRepository.updateRefreshToken(userId, authTokenResponse.refreshToken());

        return authTokenResponse;
    }

    @Transactional
    public AccessTokenResponse reIssueAccessToken(@NotNull String accessToken,
        @NotNull String refreshToken) {
        Long refreshTokenUserId = authTokenGenerator.getRefreshTokenUserId(accessToken,
            refreshToken);

        // DB에 저장된 리프레시 토큰과 비교 + userId
        UserEntity userEntity = userAuthRepository.findByIdAndRefreshTokenAndDeleteAtOrderByIdDesc(
                refreshTokenUserId, refreshToken, null)
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.REFRESH_TOKEN_NOT_CORRECT_USER,
                HttpStatus.BAD_REQUEST));

        return authTokenGenerator.reIssueAccessToken(userEntity.getId());
    }

    // @AuthenticatedUser 를 위한 메서드 - AuthenticatedUserResolver 에서 사용
    public UserEntity getAuthUserWithThrow(@NotNull Long userId) {
        return userAuthRepository.findByIdAndDeleteAtOrderByIdDesc(userId, null)
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.AUTH_USER_NOT_FOUND,
                HttpStatus.BAD_REQUEST));
    }
}
