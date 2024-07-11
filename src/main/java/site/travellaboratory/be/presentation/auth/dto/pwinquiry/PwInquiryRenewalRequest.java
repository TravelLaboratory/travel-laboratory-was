package site.travellaboratory.be.presentation.auth.dto.pwinquiry;

public record PwInquiryRenewalRequest(
    String username,
    String password,
    Long pwQuestionId,
    String answer
) {

}
