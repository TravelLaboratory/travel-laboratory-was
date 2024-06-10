package site.travellaboratory.be.controller.auth.dto;

import site.travellaboratory.be.domain.auth.pwanswer.PwAnswer;
import site.travellaboratory.be.domain.user.entity.User;

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
