package site.travellaboratory.be.presentation.auth.dto.pwinquiry;

import site.travellaboratory.be.infrastructure.domains.auth.pwanswer.entity.PwAnswerJpaEntity;
import site.travellaboratory.be.infrastructure.domains.user.entity.UserJpaEntity;

public record PwInquiryVerificationResponse(
    String username,
    Long pwQuestionId,
    String answer
) {
    public static PwInquiryVerificationResponse from(final UserJpaEntity userJpaEntity,final PwAnswerJpaEntity pwAnswerJpaEntity) {
        return new PwInquiryVerificationResponse(
            userJpaEntity.getUsername(),
            pwAnswerJpaEntity.getPwQuestionJpaEntity().getId(),
            pwAnswerJpaEntity.getAnswer()
        );
    }
}
