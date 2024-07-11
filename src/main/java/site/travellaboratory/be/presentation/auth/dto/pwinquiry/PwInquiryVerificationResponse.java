package site.travellaboratory.be.presentation.auth.dto.pwinquiry;

import site.travellaboratory.be.infrastructure.domains.auth.pwanswer.entity.PwAnswer;
import site.travellaboratory.be.infrastructure.domains.user.entity.User;

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
