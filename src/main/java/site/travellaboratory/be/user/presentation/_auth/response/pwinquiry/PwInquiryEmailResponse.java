package site.travellaboratory.be.user.presentation._auth.response.pwinquiry;

import site.travellaboratory.be.user.domain._pw.PwAnswer;

public record PwInquiryEmailResponse(
    String username,
    Long pwQuestionId
)
{
    public static PwInquiryEmailResponse from(String username, PwAnswer pwAnswer) {
        return new PwInquiryEmailResponse(
            username,
            pwAnswer.getPwQuestionId()
        );
    }
}
