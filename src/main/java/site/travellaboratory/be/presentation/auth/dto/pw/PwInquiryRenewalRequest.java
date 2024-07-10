package site.travellaboratory.be.presentation.auth.dto.pw;

public record PwInquiryRenewalRequest(
    String username,
    String password,
    Long pwQuestionId,
    String answer
) {

}
