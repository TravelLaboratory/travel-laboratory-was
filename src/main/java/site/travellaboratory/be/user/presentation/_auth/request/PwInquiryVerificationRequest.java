package site.travellaboratory.be.user.presentation._auth.request;

public record PwInquiryVerificationRequest(
    String username,
    Long pwQuestionId,
    String answer
) {

}
