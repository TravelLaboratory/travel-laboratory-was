package site.travellaboratory.be.user.application._auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.error.ErrorCodes;
import site.travellaboratory.be.user.application.port.PwAnswerRepository;
import site.travellaboratory.be.user.application.port.PwQuestionRepository;
import site.travellaboratory.be.user.application.port.UserRepository;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain._auth.request.UserJoinRequest;
import site.travellaboratory.be.user.domain._pw.PwAnswer;
import site.travellaboratory.be.user.domain._pw.PwQuestion;
import site.travellaboratory.be.user.domain._pw.enums.PwQuestionStatus;
import site.travellaboratory.be.user.domain.enums.UserStatus;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;
    private final PwQuestionRepository pwQuestionRepository;
    private final PwAnswerRepository pwAnswerRepository;

    @Transactional
    public User register(UserJoinRequest request) {

        String encodePassword = encoder.encode(request.password());
        User user = User.register(encodePassword, request);

        // 닉네임 중복 체크
        userRepository.findByNickname(request.nickname()).ifPresent(it -> {
            throw new BeApplicationException(ErrorCodes.AUTH_DUPLICATED_NICK_NAME,
                HttpStatus.CONFLICT);
        });

        // 이미 가입한 유저인지 체크
        userRepository.findByUsernameAndStatusOrderByIdDesc(request.username(), UserStatus.ACTIVE)
            .ifPresent(it -> {
                throw new BeApplicationException(ErrorCodes.AUTH_DUPLICATED_USER_NAME,
                    HttpStatus.CONFLICT);
        });

        // 새로운 유저 생성
        User savedUser = userRepository.save(user);

        //비번 질문 조회
        PwQuestion pwQuestion = pwQuestionRepository.findByIdAndStatus(request.pwQuestionId(), PwQuestionStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.PASSWORD_INVALID_QUESTION,
                HttpStatus.BAD_REQUEST));

        // 비번 답변 저장
        PwAnswer pwAnswer = PwAnswer.create(savedUser.getId(), pwQuestion.getId(), request.pwAnswer());
        pwAnswerRepository.save(pwAnswer);
        return savedUser;
    }
}
