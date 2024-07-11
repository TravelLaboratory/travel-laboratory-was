package site.travellaboratory.be.application.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.infrastructure.domains.user.UserRepository;
import site.travellaboratory.be.infrastructure.domains.user.enums.UserStatus;
import site.travellaboratory.be.presentation.auth.dto.userverification.UserNicknameRequest;
import site.travellaboratory.be.presentation.auth.dto.userverification.UserNicknameResponse;
import site.travellaboratory.be.presentation.auth.dto.userverification.UsernameRequest;
import site.travellaboratory.be.presentation.auth.dto.userverification.UsernameResponse;

@Service
@RequiredArgsConstructor
public class UserVerificationService {

    private final UserRepository userRepository;

    public UserNicknameResponse isNicknameAvailable(UserNicknameRequest request) {
        // 닉네임 중복 체크
        userRepository.findByNickname(request.nickname()).ifPresent(it -> {
            throw new BeApplicationException(ErrorCodes.AUTH_DUPLICATED_NICK_NAME,
                HttpStatus.CONFLICT);
        });

        return UserNicknameResponse.from(true);
    }

    public UsernameResponse isUsernameAvailable(final UsernameRequest request) {
        // (회원아이디) 이메일 중복 체크
        userRepository.findByUsernameAndStatusOrderByIdDesc(request.username(), UserStatus.ACTIVE).ifPresent(it -> {
            throw new BeApplicationException(ErrorCodes.AUTH_DUPLICATED_USER_NAME,
                HttpStatus.CONFLICT);
        });

        return UsernameResponse.from(true);
    }
}
