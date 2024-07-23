package site.travellaboratory.be.user.presentation._auth.response.pwinquiry;

import site.travellaboratory.be.user.domain.User;

public record PwInquiryRenewalResponse(
    Long userId
) {
    public static PwInquiryRenewalResponse from(User user) {
        return new PwInquiryRenewalResponse(
            user.getId()
        );
    }
}
