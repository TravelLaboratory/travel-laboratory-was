package site.travellaboratory.be.presentation.auth.dto.pwinquiry;

public record PwInquiryVerificationResponse(
    String username,
    Long pwQuestionId,
    String answer
) {
    public static PwInquiryVerificationResponse from(String username, Long pwQuestionId, String answer) {
        return new PwInquiryVerificationResponse(
            username, pwQuestionId, answer
        );
    }
}
