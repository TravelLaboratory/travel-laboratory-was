package site.travellaboratory.be.presentation.auth.dto.pwinquiry;

import site.travellaboratory.be.infrastructure.domains.auth.pwanswer.entity.PwAnswerJpaEntity;
import site.travellaboratory.be.infrastructure.domains.user.entity.UserJpaEntity;

public record PwInquiryEmailResponse(
    String username,
    Long pwQuestionId
)
{
    public static PwInquiryEmailResponse from(final UserJpaEntity userJpaEntity, final PwAnswerJpaEntity pwAnswerJpaEntity) {
        return new PwInquiryEmailResponse(
            userJpaEntity.getUsername(),
            pwAnswerJpaEntity.getPwQuestionJpaEntity().getId()
        );
    }
}
