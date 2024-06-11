package site.travellaboratory.be.controller.auth.dto.pw;

public record PwInquiryRenewalRequest(
    String username,
    String password,
    Long pwQuestionId,
    String answer
) {

}
