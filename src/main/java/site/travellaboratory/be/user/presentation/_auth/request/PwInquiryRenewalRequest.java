package site.travellaboratory.be.user.presentation._auth.request;

public record PwInquiryRenewalRequest(
    String username,
    String password,
    Long pwQuestionId,
    String answer
) {

}
