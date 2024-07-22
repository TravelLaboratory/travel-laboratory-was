package site.travellaboratory.be.user.presentation._auth.request;

import lombok.Builder;

public record PwInquiryEmailRequest(
    String username
) {
    @Builder
    public PwInquiryEmailRequest {
    }
}
