package site.travellaboratory.be.user.application._auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.user.domain._auth.UserAuth;
import site.travellaboratory.be.user.domain.enums.UserStatus;
import site.travellaboratory.be.user.domain._pw.PwAnswer;
import site.travellaboratory.be.user.domain._pw.PwQuestion;
import site.travellaboratory.be.user.domain._pw.enums.PwQuestionStatus;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.infrastructure.persistence.repository.PwAnswerJpaRepository;
import site.travellaboratory.be.user.infrastructure.persistence.entity.PwAnswerEntity;
import site.travellaboratory.be.user.infrastructure.persistence.repository.PwQuestionJpaRepository;
import site.travellaboratory.be.user.infrastructure.persistence.repository.UserJpaRepository;
import site.travellaboratory.be.user.infrastructure.persistence.entity.UserEntity;
import site.travellaboratory.be.user.domain._auth.request.UserJoinRequest;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private final BCryptPasswordEncoder encoder;
    private final UserJpaRepository userJpaRepository;
    private final PwQuestionJpaRepository pwQuestionJpaRepository;
    private final PwAnswerJpaRepository pwAnswerJpaRepository;

    @Transactional
    public User register(UserJoinRequest request) {

        String encodePassword = encoder.encode(request.password());
        UserAuth userAuth = UserAuth.register(encodePassword, request);

        // 닉네임 중복 체크
        userJpaRepository.findByNickname(request.nickname()).ifPresent(it -> {
            throw new BeApplicationException(ErrorCodes.AUTH_DUPLICATED_NICK_NAME,
                HttpStatus.CONFLICT);
        });

        // 이미 가입한 유저인지 체크
        userJpaRepository.findByUsernameAndStatusOrderByIdDesc(request.username(), UserStatus.ACTIVE)
            .ifPresent(it -> {
                throw new BeApplicationException(ErrorCodes.AUTH_DUPLICATED_USER_NAME,
                    HttpStatus.CONFLICT);
        });

        // 새로운 유저 생성
        User user = User.create(request.nickname());
        User savedUser = userJpaRepository.save(UserEntity.from(user, userAuth)).toModel();

        //비번 질문 조회
        PwQuestion pwQuestion = pwQuestionJpaRepository.findByIdAndStatus(
                request.pwQuestionId(),
                PwQuestionStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.PASSWORD_INVALID_QUESTION,
                HttpStatus.BAD_REQUEST)).toModel();

        // 비번 답변 저장
        PwAnswer pwAnswer = PwAnswer.create(savedUser.getId(), pwQuestion.getId(), request.pwAnswer());
        pwAnswerJpaRepository.save(PwAnswerEntity.from(pwAnswer));
        return savedUser;
    }
}
