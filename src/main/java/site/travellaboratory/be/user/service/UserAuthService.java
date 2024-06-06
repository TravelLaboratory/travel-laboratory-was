package site.travellaboratory.be.user.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.jwt.model.JwtToken;
import site.travellaboratory.be.jwt.model.Token;
import site.travellaboratory.be.jwt.service.TokenService;
import site.travellaboratory.be.user.repository.UserAuthRepository;
import site.travellaboratory.be.user.repository.entity.UserEntity;
import site.travellaboratory.be.user.service.domain.User;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final BCryptPasswordEncoder encoder;
    private final TokenService tokenService;
    private final UserAuthRepository userAuthRepository;

    @Transactional
    public User join(
        @NotNull String userName,
        @NotNull String password,
        @NotNull String nickName
    ) {
        // 이미 가입한 유저인지 체크
        userAuthRepository.findByUserNameAndDeleteAtOrderByIdDesc(userName, null).ifPresent(it -> {
            throw new BeApplicationException(ErrorCodes.AUTH_DUPLICATED_USER_NAME, HttpStatus.CONFLICT);
        });

        // 닉네임 중복 체크
        userAuthRepository.findByNickName(nickName).ifPresent(it -> {
            throw new BeApplicationException(ErrorCodes.AUTH_DUPLICATED_NICK_NAME, HttpStatus.CONFLICT);
        });
        UserEntity userEntity = userAuthRepository.save(UserEntity.of(userName, encoder.encode(password), nickName));
        return mapToDomain(userEntity);
    }

    @Transactional
    public JwtToken login(
        @NotNull String userName,
        @NotNull String password
    ) {
        // 회원가입 여부 체크
        UserEntity userEntity = userAuthRepository.findByUserNameAndDeleteAtOrderByIdDesc(userName, null)
            .orElseThrow(() -> new BeApplicationException(
                ErrorCodes.AUTH_USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        // 비밀번호 체크
        if (!encoder.matches(password, userEntity.getPassword())) {
            throw new BeApplicationException(ErrorCodes.AUTH_INVALID_PASSWORD, HttpStatus.UNAUTHORIZED);
        }

        // 토큰 생성
        Long userId = userEntity.getId();
        Token accessToken = tokenService.issueAccessToken(userId);
        Token refreshToken = tokenService.issueRefreshToken(userId);

        // 리프레시 토큰은 db에 저장
        userAuthRepository.updateRefreshToken(userId, refreshToken.getToken());
        return new JwtToken(accessToken, refreshToken);
    }

    // @AuthenticatedUser 를 위한 메서드 - AuthenticatedUserResolver 에서 사용
    public UserEntity getAuthUserWithThrow(@NotNull Long userId) {
        return userAuthRepository.findByIdAndDeleteAtOrderByIdDesc(userId, null)
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.AUTH_USER_NOT_FOUND, HttpStatus.BAD_REQUEST));
    }

    private static User mapToDomain(UserEntity entity) {
        return new User(
            entity.getId(),
            entity.getUserName(),
            entity.getPassword(),
            entity.getRole(),
            entity.getNickName(),
            entity.getProfileImgUrl(),
            entity.getRegisterAt(),
            entity.getUpdateAt(),
            entity.getDeleteAt(),
            entity.getRefreshToken()
        );
    }

}
