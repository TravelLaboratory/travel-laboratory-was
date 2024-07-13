package site.travellaboratory.be.application.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.domain.user.enums.UserStatus;
import site.travellaboratory.be.infrastructure.domains.user.UserJpaRepository;
import site.travellaboratory.be.presentation.auth.dto.userverification.UserNicknameRequest;
import site.travellaboratory.be.presentation.auth.dto.userverification.UsernameRequest;

@Service
@RequiredArgsConstructor
public class UserVerificationService {

    private final UserJpaRepository userJpaRepository;

    public Boolean isNicknameAvailable(UserNicknameRequest request) {
        // 닉네임 중복 체크
        userJpaRepository.findByNickname(request.nickname()).ifPresent(it -> {
            throw new BeApplicationException(ErrorCodes.AUTH_DUPLICATED_NICK_NAME,
                HttpStatus.CONFLICT);
        });

        return true;
    }

    public Boolean isUsernameAvailable(final UsernameRequest request) {
        // (회원아이디) 이메일 중복 체크
        userJpaRepository.findByUsernameAndStatusOrderByIdDesc(request.username(), UserStatus.ACTIVE).ifPresent(it -> {
            throw new BeApplicationException(ErrorCodes.AUTH_DUPLICATED_USER_NAME,
                HttpStatus.CONFLICT);
        });

        return true;
    }
}
