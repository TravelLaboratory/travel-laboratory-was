package site.travellaboratory.be.user.presentation._auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.common.presentation.response.ApiResponse;
import site.travellaboratory.be.user.application._auth.PwInquiryService;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain._pw.PwAnswer;
import site.travellaboratory.be.user.presentation._auth.request.PwInquiryEmailRequest;
import site.travellaboratory.be.user.presentation._auth.request.PwInquiryRenewalRequest;
import site.travellaboratory.be.user.presentation._auth.request.PwInquiryVerificationRequest;
import site.travellaboratory.be.user.presentation._auth.response.pwinquiry.PwInquiryEmailResponse;
import site.travellaboratory.be.user.presentation._auth.response.pwinquiry.PwInquiryRenewalResponse;
import site.travellaboratory.be.user.presentation._auth.response.pwinquiry.PwInquiryVerificationResponse;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PwInquiryController {

    private final PwInquiryService pwInquiryService;

    @PostMapping("/auth/pw-inquiry/email")
    public ApiResponse<PwInquiryEmailResponse> pwInquiryEmail(
        @RequestBody PwInquiryEmailRequest pwInquiryEmailRequest
    ) {
        PwAnswer result = pwInquiryService.pwInquiryEmail(pwInquiryEmailRequest);
        PwInquiryEmailResponse response = PwInquiryEmailResponse.from(pwInquiryEmailRequest.username(), result);
        return ApiResponse.OK(response);
    }

    @PostMapping("/auth/pw-inquiry/verification")
    public ApiResponse<PwInquiryVerificationResponse> pwInquiryVerification(
        @RequestBody PwInquiryVerificationRequest pwInquiryVerificationRequest
    ) {

        PwAnswer result = pwInquiryService.pwInquiryVerification(pwInquiryVerificationRequest);
        PwInquiryVerificationResponse response = PwInquiryVerificationResponse.from(
            pwInquiryVerificationRequest.username(),
            result.getPwQuestionId(), result.getAnswer());
        return ApiResponse.OK(response);

    }

    @PostMapping("/auth/pw-inquiry/renewal")
    public ApiResponse<PwInquiryRenewalResponse> pwInquiryRenewal(
        @RequestBody PwInquiryRenewalRequest pwInquiryRenewalRequest
    ) {
        User user = pwInquiryService.pwInquiryRenewal(pwInquiryRenewalRequest);
        return ApiResponse.OK(PwInquiryRenewalResponse.from(user));
    }
}

