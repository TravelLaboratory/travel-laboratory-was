package site.travellaboratory.be.application.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.domain.user.auth.UserAuth;
import site.travellaboratory.be.domain.user.enums.UserStatus;
import site.travellaboratory.be.domain.user.pw.PwAnswer;
import site.travellaboratory.be.domain.user.pw.PwQuestion;
import site.travellaboratory.be.domain.user.pw.enums.PwQuestionStatus;
import site.travellaboratory.be.domain.user.user.User;
import site.travellaboratory.be.infrastructure.domains.auth.pwanswer.PwAnswerRepository;
import site.travellaboratory.be.infrastructure.domains.auth.pwanswer.entity.PwAnswerJpaEntity;
import site.travellaboratory.be.infrastructure.domains.auth.pwquestion.PwQuestionRepository;
import site.travellaboratory.be.infrastructure.domains.user.UserRepository;
import site.travellaboratory.be.infrastructure.domains.user.entity.UserJpaEntity;
import site.travellaboratory.be.presentation.auth.dto.userregistration.UserJoinRequest;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;
    private final PwQuestionRepository pwQuestionRepository;
    private final PwAnswerRepository pwAnswerRepository;

    @Transactional
    public User register(UserJoinRequest request) {

        UserAuth userAuth = UserAuth.create(request.username(), encoder.encode(
            request.password()), request.isAgreement());

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
        User user = User.create(request.nickname());
        User savedUser = userRepository.save(UserJpaEntity.from(user, userAuth)).toModel();

        //비번 질문 조회
        PwQuestion pwQuestion = pwQuestionRepository.findByIdAndStatus(
                request.pwQuestionId(),
                PwQuestionStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.PASSWORD_INVALID_QUESTION,
                HttpStatus.BAD_REQUEST)).toModel();

        // 비번 답변 저장
        PwAnswer pwAnswer = PwAnswer.create(savedUser.getId(), pwQuestion.getId(), request.pwAnswer());
        pwAnswerRepository.save(PwAnswerJpaEntity.from(pwAnswer));
        return savedUser;
    }
}
