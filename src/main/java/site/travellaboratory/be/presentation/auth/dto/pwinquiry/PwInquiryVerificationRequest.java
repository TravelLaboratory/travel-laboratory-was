package site.travellaboratory.be.presentation.auth.dto.pwinquiry;

public record PwInquiryVerificationRequest(
    String username,
    Long pwQuestionId,
    String answer
) {

}
