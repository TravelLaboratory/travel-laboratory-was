package site.travellaboratory.be.presentation.auth.dto.pw;

public record PwInquiryVerificationRequest(
    String username,
    Long pwQuestionId,
    String answer
) {

}
