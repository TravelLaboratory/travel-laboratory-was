package site.travellaboratory.be.user.presentation._auth.response.pwinquiry;

public record PwInquiryVerificationRequest(
    String username,
    Long pwQuestionId,
    String answer
) {

}
