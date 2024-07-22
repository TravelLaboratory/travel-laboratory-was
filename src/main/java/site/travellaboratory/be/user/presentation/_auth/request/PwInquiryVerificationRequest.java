package site.travellaboratory.be.user.presentation._auth.request;

import lombok.Builder;

public record PwInquiryVerificationRequest(
    String username,
    Long pwQuestionId,
    String answer
) {
    @Builder
    public PwInquiryVerificationRequest {
    }
}
