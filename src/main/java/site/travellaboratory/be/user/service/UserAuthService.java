package site.travellaboratory.be.user.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.user.repository.UserAuthRepository;
import site.travellaboratory.be.user.repository.entity.UserEntity;
import site.travellaboratory.be.user.service.domain.User;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final UserAuthRepository userAuthRepository;
    private final BCryptPasswordEncoder encoder;


    @Transactional
    public User join(
        @NotNull String userName,
        @NotNull String password,
        @NotNull String nickName
    ) {
        // 이미 가입한 유저인지 체크
        userAuthRepository.findByUserName(userName).ifPresent(it -> {
            throw new BeApplicationException(ErrorCodes.AUTH_DUPLICATED_USER_NAME, HttpStatus.CONFLICT);
        });

        // 닉네임 중복 체크
        userAuthRepository.findByNickName(nickName).ifPresent(it -> {
            throw new BeApplicationException(ErrorCodes.AUTH_DUPLICATED_NICK_NAME, HttpStatus.CONFLICT);
        });

        UserEntity userEntity = userAuthRepository.save(UserEntity.of(userName, encoder.encode(password), nickName));
        return mapToDomain(userEntity);
    }

    // todo : implement
    public String login(
        @NotNull String userName,
        @NotNull String password
    ) {
        // 회원가입 여부 체크
        UserEntity userEntity = userAuthRepository.findByUserName(userName)
            .orElseThrow(() -> new BeApplicationException(
                ErrorCodes.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        // 비밀번호 체크
        if (userEntity.getPassword().equals(password)) {
            throw new BeApplicationException(ErrorCodes.USER_INCORRECT_PASSWORD,
                HttpStatus.UNAUTHORIZED);
        }

        // 토큰 생성


        return "";
    }

    public static User mapToDomain(UserEntity entity) {
        return new User(
            entity.getId(),
            entity.getUserName(),
            entity.getPassword(),
            entity.getRole(),
            entity.getNickName(),
            entity.getProfileImgUrl(),
            entity.getRegisterAt(),
            entity.getUpdateAt(),
            entity.getDeleteAt()
        );
    }
}
