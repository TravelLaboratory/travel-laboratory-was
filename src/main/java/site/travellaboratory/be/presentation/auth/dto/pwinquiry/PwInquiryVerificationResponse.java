package site.travellaboratory.be.presentation.auth.dto.pwinquiry;

import site.travellaboratory.be.infrastructure.domains.auth.pwanswer.entity.PwAnswer;
import site.travellaboratory.be.infrastructure.domains.user.entity.UserJpaEntity;

public record PwInquiryVerificationResponse(
    String username,
    Long pwQuestionId,
    String answer
) {
    public static PwInquiryVerificationResponse from(final UserJpaEntity userJpaEntity,final PwAnswer pwAnswer) {
        return new PwInquiryVerificationResponse(
            userJpaEntity.getUsername(),
            pwAnswer.getPwQuestion().getId(),
            pwAnswer.getAnswer()
        );
    }
}
