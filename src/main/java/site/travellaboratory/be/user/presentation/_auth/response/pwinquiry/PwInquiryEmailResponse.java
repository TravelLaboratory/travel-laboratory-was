package site.travellaboratory.be.user.presentation._auth.response.pwinquiry;

public record PwInquiryEmailResponse(
    String username,
    Long pwQuestionId
)
{
    public static PwInquiryEmailResponse from(String username, Long pwQuestionId) {
        return new PwInquiryEmailResponse(
            username,
            pwQuestionId
        );
    }
}
