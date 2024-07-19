package site.travellaboratory.be.user.presentation._auth.response.pwinquiry;

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
