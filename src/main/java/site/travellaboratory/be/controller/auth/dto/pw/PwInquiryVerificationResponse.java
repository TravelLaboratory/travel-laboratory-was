package site.travellaboratory.be.controller.auth.dto.pw;

import site.travellaboratory.be.domain.auth.pwanswer.PwAnswer;
import site.travellaboratory.be.domain.user.entity.User;

public record PwInquiryVerificationResponse(
    String username,
    Long pwQuestionId,
    String answer
) {
    public static PwInquiryVerificationResponse from(final User user,final PwAnswer pwAnswer) {
        return new PwInquiryVerificationResponse(
            user.getUsername(),
            pwAnswer.getPwQuestion().getId(),
            pwAnswer.getAnswer()
        );
    }
}
