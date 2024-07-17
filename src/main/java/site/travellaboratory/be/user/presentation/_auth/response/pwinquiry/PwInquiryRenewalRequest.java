package site.travellaboratory.be.user.presentation._auth.response.pwinquiry;

public record PwInquiryRenewalRequest(
    String username,
    String password,
    Long pwQuestionId,
    String answer
) {

}
