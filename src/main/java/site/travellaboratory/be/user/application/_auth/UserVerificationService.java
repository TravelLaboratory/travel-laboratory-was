package site.travellaboratory.be.user.application._auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.error.ErrorCodes;
import site.travellaboratory.be.user.application.port.UserRepository;
import site.travellaboratory.be.user.domain._auth.request.UserNicknameRequest;
import site.travellaboratory.be.user.domain._auth.request.UsernameRequest;
import site.travellaboratory.be.user.domain.enums.UserStatus;

@Service
@RequiredArgsConstructor
public class UserVerificationService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Boolean isNicknameAvailable(UserNicknameRequest request) {
        // 닉네임 중복 체크
        userRepository.findByNickname(request.nickname()).ifPresent(it -> {
            throw new BeApplicationException(ErrorCodes.AUTH_DUPLICATED_NICK_NAME,
                HttpStatus.CONFLICT);
        });

        return true;
    }

    @Transactional(readOnly = true)
    public Boolean isUsernameAvailable(final UsernameRequest request) {
        // (회원아이디) 이메일 중복 체크
        userRepository.findByUsernameAndStatusOrderByIdDesc(request.username(), UserStatus.ACTIVE).ifPresent(it -> {
            throw new BeApplicationException(ErrorCodes.AUTH_DUPLICATED_USER_NAME,
                HttpStatus.CONFLICT);
        });

        return true;
    }
}
