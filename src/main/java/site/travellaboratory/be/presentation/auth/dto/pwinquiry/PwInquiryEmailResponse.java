package site.travellaboratory.be.presentation.auth.dto.pwinquiry;

import site.travellaboratory.be.infrastructure.domains.auth.pwanswer.entity.PwAnswer;
import site.travellaboratory.be.infrastructure.domains.user.entity.UserJpaEntity;

public record PwInquiryEmailResponse(
    String username,
    Long pwQuestionId
)
{
    public static PwInquiryEmailResponse from(final UserJpaEntity userJpaEntity, final PwAnswer pwAnswer) {
        return new PwInquiryEmailResponse(
            userJpaEntity.getUsername(),
            pwAnswer.getPwQuestion().getId()
        );
    }
}
