package site.travellaboratory.be.user.presentation._auth.request;

import lombok.Builder;

public record PwInquiryRenewalRequest(
    String username,
    String password,
    Long pwQuestionId,
    String answer
) {
    @Builder
    public PwInquiryRenewalRequest {
    }
}
