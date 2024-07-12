package site.travellaboratory.be.presentation.auth.dto.pwinquiry;

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
