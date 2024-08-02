package site.travellaboratory.be.user.presentation._auth.response.pwinquiry;

import site.travellaboratory.be.user.domain.User;

public record PwInquiryRenewalResponse(
    Boolean isRenewal
) {
    public static PwInquiryRenewalResponse from(User user) {
        return new PwInquiryRenewalResponse(
            user != null
        );
    }
}
