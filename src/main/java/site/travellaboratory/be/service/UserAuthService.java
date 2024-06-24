package site.travellaboratory.be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.controller.auth.dto.UserJoinRequest;
import site.travellaboratory.be.controller.auth.dto.UserJoinResponse;
import site.travellaboratory.be.controller.auth.dto.UserLoginRequest;
import site.travellaboratory.be.controller.auth.dto.UserLoginResponse;
import site.travellaboratory.be.controller.auth.dto.UserNicknameRequest;
import site.travellaboratory.be.controller.auth.dto.UserNicknameResponse;
import site.travellaboratory.be.controller.auth.dto.UsernameRequest;
import site.travellaboratory.be.controller.auth.dto.UsernameResponse;
import site.travellaboratory.be.controller.auth.dto.pw.PwInquiryEmailRequest;
import site.travellaboratory.be.controller.auth.dto.pw.PwInquiryEmailResponse;
import site.travellaboratory.be.controller.auth.dto.pw.PwInquiryRenewalRequest;
import site.travellaboratory.be.controller.auth.dto.pw.PwInquiryVerificationRequest;
import site.travellaboratory.be.controller.auth.dto.pw.PwInquiryVerificationResponse;
import site.travellaboratory.be.controller.jwt.dto.AccessTokenResponse;
import site.travellaboratory.be.controller.jwt.dto.AuthTokenResponse;
import site.travellaboratory.be.controller.jwt.util.AuthTokenGenerator;
import site.travellaboratory.be.domain.auth.pwanswer.PwAnswer;
import site.travellaboratory.be.domain.auth.pwanswer.PwAnswerRepository;
import site.travellaboratory.be.domain.auth.pwanswer.enums.PwAnswerStatus;
import site.travellaboratory.be.domain.auth.pwquestion.PwQuestion;
import site.travellaboratory.be.domain.auth.pwquestion.PwQuestionRepository;
import site.travellaboratory.be.domain.auth.pwquestion.enums.PwQuestionStatus;
import site.travellaboratory.be.domain.user.UserRepository;
import site.travellaboratory.be.domain.user.entity.User;
import site.travellaboratory.be.domain.user.entity.UserStatus;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final BCryptPasswordEncoder encoder;
    private final AuthTokenGenerator authTokenGenerator;
    private final UserRepository userRepository;
    private final PwQuestionRepository pwQuestionRepository;
    private final PwAnswerRepository pwAnswerRepository;

    @Transactional
    public UserJoinResponse join(UserJoinRequest request) {
        // 개인정보 동의 유무
        if (!request.isAgreement()) {
            throw new BeApplicationException(ErrorCodes.AUTH_USER_NOT_IS_AGREEMENT,
                HttpStatus.BAD_REQUEST);
        }

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

        // 새로운 유저 생성
        User user = userRepository.save(
            User.of(request.username(), encoder.encode(
                request.password()), request.nickname(), true));

        //비번 질문 조회
        PwQuestion pwQuestion = pwQuestionRepository.findByIdAndStatus(request.pwQuestionId(),
                PwQuestionStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.PASSWORD_INVALID_QUESTION,
                HttpStatus.BAD_REQUEST));

        // 비번 답변 저장
        PwAnswer pwAnswer = PwAnswer.of(user, pwQuestion, request.pwAnswer());
        pwAnswerRepository.save(pwAnswer);

        return UserJoinResponse.from(user);
    }

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

    @Transactional
    public UserLoginResponse login(UserLoginRequest request) {
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

        // profile_img_url 전송
        // 여기서 orElseThrow 에 갈일은 위에서 회원가입 여부를 체크하기 없음
        User user = userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(
                ErrorCodes.AUTH_USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        AuthTokenResponse authTokenResponse = authTokenGenerator.generateTokens(userId);

        return UserLoginResponse.from(user, authTokenResponse);
    }


    public AccessTokenResponse reIssueAccessToken(
        final String accessToken,
        final String refreshToken) {
        return authTokenGenerator.reIssueAccessToken(accessToken, refreshToken);
    }

    public PwInquiryEmailResponse pwInquiryEmail(final PwInquiryEmailRequest request) {
        User user = userRepository.findByUsernameAndStatusOrderByIdDesc(request.username(), UserStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(
                ErrorCodes.PASSWORD_INVALID_EMAIL, HttpStatus.NOT_FOUND));

        // 기획상 Optional 일 수 없다.
        PwAnswer pwAnswer = pwAnswerRepository.findByUserIdAndStatus(user.getId(),
            PwAnswerStatus.ACTIVE);

        return PwInquiryEmailResponse.from(user, pwAnswer);
    }

    public PwInquiryVerificationResponse pwInquiryVerification(final PwInquiryVerificationRequest request) {
        // 해당 이메일의 유저가 존재하는지
        User user = userRepository.findByUsernameAndStatusOrderByIdDesc(request.username(), UserStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(
                ErrorCodes.PASSWORD_INVALID_EMAIL, HttpStatus.NOT_FOUND));

        // 답변이 일치한지 판단
        PwAnswer pwAnswer = pwAnswerRepository.findByUserIdAndPwQuestionIdAndAnswerAndStatus(
                user.getId(),
                request.pwQuestionId(), request.answer(), PwAnswerStatus.ACTIVE)
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.PASSWORD_INQUIRY_INVALID_ANSWER,
                    HttpStatus.UNAUTHORIZED));

        return PwInquiryVerificationResponse.from(user, pwAnswer);
    }

    @Transactional
    public void pwInquiryRenewal(final PwInquiryRenewalRequest request) {
        // 해당 이메일의 유저가 존재하는지
        User user = userRepository.findByUsernameAndStatusOrderByIdDesc(request.username(), UserStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(
                ErrorCodes.PASSWORD_INVALID_EMAIL, HttpStatus.NOT_FOUND));

        // 답변이 일치한지 판단
        pwAnswerRepository.findByUserIdAndPwQuestionIdAndAnswerAndStatus(
                user.getId(), request.pwQuestionId(), request.answer(), PwAnswerStatus.ACTIVE)
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.PASSWORD_INQUIRY_INVALID_ANSWER,
                    HttpStatus.UNAUTHORIZED));

        // 비밀번호 암호화 및 업데이트 - 저장
        String encodedPassword = encoder.encode(request.password());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }
}
