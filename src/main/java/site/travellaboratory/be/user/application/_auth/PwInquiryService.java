package site.travellaboratory.be.user.application._auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.presentation.exception.BeApplicationException;
import site.travellaboratory.be.common.presentation.error.ErrorCodes;
import site.travellaboratory.be.user.application.port.PwAnswerRepository;
import site.travellaboratory.be.user.application.port.UserRepository;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain._pw.PwAnswer;
import site.travellaboratory.be.user.domain._pw.enums.PwAnswerStatus;
import site.travellaboratory.be.user.domain.enums.UserStatus;
import site.travellaboratory.be.user.presentation._auth.request.PwInquiryEmailRequest;
import site.travellaboratory.be.user.presentation._auth.request.PwInquiryRenewalRequest;
import site.travellaboratory.be.user.presentation._auth.request.PwInquiryVerificationRequest;

@Service
@RequiredArgsConstructor
public class PwInquiryService {

    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;
    private final PwAnswerRepository pwAnswerRepository;

    public PwAnswer pwInquiryEmail(PwInquiryEmailRequest request) {
        User user = userRepository.findByUsernameAndStatusOrderByIdDesc(request.username(),
                UserStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(
                ErrorCodes.PASSWORD_INVALID_EMAIL, HttpStatus.NOT_FOUND));

        // 기획상 Optional 일 수 없다.
        final PwAnswer pwAnswer = pwAnswerRepository.getByUserIdAndStatus(user.getId(), PwAnswerStatus.ACTIVE);

        return pwAnswer;
    }


    public PwAnswer pwInquiryVerification(final PwInquiryVerificationRequest request) {
        // 해당 이메일의 유저가 존재하는지
        User user = userRepository.findByUsernameAndStatusOrderByIdDesc(request.username(),
                UserStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(
                ErrorCodes.PASSWORD_INVALID_EMAIL, HttpStatus.NOT_FOUND));

        // 답변이 일치한지 판단
        final PwAnswer pwAnswer = pwAnswerRepository.findByUserIdAndPwQuestionIdAndAnswerAndStatus(
                user.getId(), request.pwQuestionId(), request.answer(), PwAnswerStatus.ACTIVE)
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.PASSWORD_INQUIRY_INVALID_ANSWER,
                    HttpStatus.UNAUTHORIZED));

        return pwAnswer;
    }

    @Transactional
    public User pwInquiryRenewal(final PwInquiryRenewalRequest request) {
        // 해당 이메일의 유저가 존재하는지
        User user = userRepository.findByUsernameAndStatusOrderByIdDesc(request.username(),
                UserStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(
                ErrorCodes.PASSWORD_INVALID_EMAIL, HttpStatus.NOT_FOUND));

        // 답변이 일치한지 판단
        pwAnswerRepository.findByUserIdAndPwQuestionIdAndAnswerAndStatus(
                user.getId(), request.pwQuestionId(), request.answer(), PwAnswerStatus.ACTIVE)
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.PASSWORD_INQUIRY_INVALID_ANSWER,
                    HttpStatus.UNAUTHORIZED));

        // 비밀번호 암호화 및 업데이트 - 저장
        String newPassword = encoder.encode(request.password());
        User withPasswordUser = user.withPassword(newPassword);
        return userRepository.save(withPasswordUser);
    }
}