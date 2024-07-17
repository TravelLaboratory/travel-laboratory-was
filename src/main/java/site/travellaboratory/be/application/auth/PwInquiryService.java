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
import site.travellaboratory.be.domain.user.pw.enums.PwAnswerStatus;
import site.travellaboratory.be.domain.user.user.User;
import site.travellaboratory.be.infrastructure.domains.auth.pwanswer.PwAnswerJpaRepository;
import site.travellaboratory.be.infrastructure.domains.user.UserJpaRepository;
import site.travellaboratory.be.infrastructure.domains.user.entity.UserEntity;
import site.travellaboratory.be.presentation.auth.dto.pwinquiry.PwInquiryEmailRequest;
import site.travellaboratory.be.presentation.auth.dto.pwinquiry.PwInquiryEmailResponse;
import site.travellaboratory.be.presentation.auth.dto.pwinquiry.PwInquiryRenewalRequest;
import site.travellaboratory.be.presentation.auth.dto.pwinquiry.PwInquiryVerificationRequest;
import site.travellaboratory.be.presentation.auth.dto.pwinquiry.PwInquiryVerificationResponse;

@Service
@RequiredArgsConstructor
public class PwInquiryService {

    private final BCryptPasswordEncoder encoder;
    private final UserJpaRepository userJpaRepository;
    private final PwAnswerJpaRepository pwAnswerJpaRepository;

    public PwInquiryEmailResponse pwInquiryEmail(PwInquiryEmailRequest request) {
        UserAuth userAuth = userJpaRepository.findByUsernameAndStatusOrderByIdDesc(request.username(),
                UserStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(
                ErrorCodes.PASSWORD_INVALID_EMAIL, HttpStatus.NOT_FOUND)).toModelUserAuth();

        // 기획상 Optional 일 수 없다.
        PwAnswer pwAnswer = pwAnswerJpaRepository.findByUserIdAndStatus(userAuth.getId(),
            PwAnswerStatus.ACTIVE).toModel();

        return PwInquiryEmailResponse.from(userAuth.getUsername(), pwAnswer.getPwQuestionId());
    }


    public PwInquiryVerificationResponse pwInquiryVerification(final PwInquiryVerificationRequest request) {
        // 해당 이메일의 유저가 존재하는지
        UserAuth userAuth = userJpaRepository.findByUsernameAndStatusOrderByIdDesc(request.username(), UserStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(
                ErrorCodes.PASSWORD_INVALID_EMAIL, HttpStatus.NOT_FOUND)).toModelUserAuth();

        // 답변이 일치한지 판단
        PwAnswer pwAnswer = pwAnswerJpaRepository.findByUserIdAndPwQuestionIdAndAnswerAndStatus(
                userAuth.getId(), request.pwQuestionId(), request.answer(), PwAnswerStatus.ACTIVE)
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.PASSWORD_INQUIRY_INVALID_ANSWER,
                    HttpStatus.UNAUTHORIZED)).toModel();

        return PwInquiryVerificationResponse.from(userAuth.getUsername(), pwAnswer.getPwQuestionId(),
            pwAnswer.getAnswer());
    }

    @Transactional
    public void pwInquiryRenewal(final PwInquiryRenewalRequest request) {
        // 해당 이메일의 유저가 존재하는지
        UserEntity userEntity = userJpaRepository.findByUsernameAndStatusOrderByIdDesc(
                request.username(), UserStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(
                ErrorCodes.PASSWORD_INVALID_EMAIL, HttpStatus.NOT_FOUND));

        User user = userEntity.toModel();
        UserAuth userAuth = userEntity.toModelUserAuth();

        // 답변이 일치한지 판단
        pwAnswerJpaRepository.findByUserIdAndPwQuestionIdAndAnswerAndStatus(
                userAuth.getId(), request.pwQuestionId(), request.answer(), PwAnswerStatus.ACTIVE)
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.PASSWORD_INQUIRY_INVALID_ANSWER,
                    HttpStatus.UNAUTHORIZED));

        // 비밀번호 암호화 및 업데이트 - 저장
        String newPassword = encoder.encode(request.password());
        UserAuth newUserAuth = userAuth.withPassword(newPassword);
        userJpaRepository.save(UserEntity.from(user, newUserAuth));
    }
}
