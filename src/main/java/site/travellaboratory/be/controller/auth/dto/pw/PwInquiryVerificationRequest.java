package site.travellaboratory.be.controller.auth.dto.pw;

public record PwInquiryVerificationRequest(
    String username,
    Long pwQuestionId,
    String answer
) {

}
