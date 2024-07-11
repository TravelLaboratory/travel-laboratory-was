package site.travellaboratory.be.presentation.auth.dto.pwinquiry;

import site.travellaboratory.be.infrastructure.auth.pwanswer.entity.PwAnswer;
import site.travellaboratory.be.infrastructure.user.entity.User;

public record PwInquiryEmailResponse(
    String username,
    Long pwQuestionId
)
{
    public static PwInquiryEmailResponse from(final User user, final PwAnswer pwAnswer) {
        return new PwInquiryEmailResponse(
            user.getUsername(),
            pwAnswer.getPwQuestion().getId()
        );
    }
}
