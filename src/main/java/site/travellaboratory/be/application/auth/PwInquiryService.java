package site.travellaboratory.be.application.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.infrastructure.domains.auth.pwanswer.PwAnswerRepository;
import site.travellaboratory.be.infrastructure.domains.auth.pwanswer.entity.PwAnswer;
import site.travellaboratory.be.infrastructure.domains.auth.pwanswer.enums.PwAnswerStatus;
import site.travellaboratory.be.infrastructure.domains.user.UserRepository;
import site.travellaboratory.be.infrastructure.domains.user.entity.UserJpaEntity;
import site.travellaboratory.be.domain.user.enums.UserStatus;
import site.travellaboratory.be.presentation.auth.dto.pwinquiry.PwInquiryEmailRequest;
import site.travellaboratory.be.presentation.auth.dto.pwinquiry.PwInquiryEmailResponse;
import site.travellaboratory.be.presentation.auth.dto.pwinquiry.PwInquiryRenewalRequest;
import site.travellaboratory.be.presentation.auth.dto.pwinquiry.PwInquiryVerificationRequest;
import site.travellaboratory.be.presentation.auth.dto.pwinquiry.PwInquiryVerificationResponse;

@Service
@RequiredArgsConstructor
public class PwInquiryService {

    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;
    private final PwAnswerRepository pwAnswerRepository;

    public PwInquiryEmailResponse pwInquiryEmail(final PwInquiryEmailRequest request) {
        UserJpaEntity userJpaEntity = userRepository.findByUsernameAndStatusOrderByIdDesc(request.username(), UserStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(
                ErrorCodes.PASSWORD_INVALID_EMAIL, HttpStatus.NOT_FOUND));

        // 기획상 Optional 일 수 없다.
        PwAnswer pwAnswer = pwAnswerRepository.findByUserIdAndStatus(userJpaEntity.getId(),
            PwAnswerStatus.ACTIVE);

        return PwInquiryEmailResponse.from(userJpaEntity, pwAnswer);
    }

    public PwInquiryVerificationResponse pwInquiryVerification(final PwInquiryVerificationRequest request) {
        // 해당 이메일의 유저가 존재하는지
        UserJpaEntity userJpaEntity = userRepository.findByUsernameAndStatusOrderByIdDesc(request.username(), UserStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(
                ErrorCodes.PASSWORD_INVALID_EMAIL, HttpStatus.NOT_FOUND));

        // 답변이 일치한지 판단
        PwAnswer pwAnswer = pwAnswerRepository.findByUserIdAndPwQuestionIdAndAnswerAndStatus(
                userJpaEntity.getId(),
                request.pwQuestionId(), request.answer(), PwAnswerStatus.ACTIVE)
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.PASSWORD_INQUIRY_INVALID_ANSWER,
                    HttpStatus.UNAUTHORIZED));

        return PwInquiryVerificationResponse.from(userJpaEntity, pwAnswer);
    }

    @Transactional
    public void pwInquiryRenewal(final PwInquiryRenewalRequest request) {
        // 해당 이메일의 유저가 존재하는지
        UserJpaEntity userJpaEntity = userRepository.findByUsernameAndStatusOrderByIdDesc(request.username(), UserStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(
                ErrorCodes.PASSWORD_INVALID_EMAIL, HttpStatus.NOT_FOUND));

        // 답변이 일치한지 판단
        pwAnswerRepository.findByUserIdAndPwQuestionIdAndAnswerAndStatus(
                userJpaEntity.getId(), request.pwQuestionId(), request.answer(), PwAnswerStatus.ACTIVE)
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.PASSWORD_INQUIRY_INVALID_ANSWER,
                    HttpStatus.UNAUTHORIZED));

        // 비밀번호 암호화 및 업데이트 - 저장
        String encodedPassword = encoder.encode(request.password());
        userJpaEntity.setPassword(encodedPassword);
        userRepository.save(userJpaEntity);
    }
}
