package site.travellaboratory.be.presentation.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.application.auth.PwInquiryService;
import site.travellaboratory.be.presentation.auth.dto.pwinquiry.PwInquiryEmailRequest;
import site.travellaboratory.be.presentation.auth.dto.pwinquiry.PwInquiryEmailResponse;
import site.travellaboratory.be.presentation.auth.dto.pwinquiry.PwInquiryRenewalRequest;
import site.travellaboratory.be.presentation.auth.dto.pwinquiry.PwInquiryVerificationRequest;
import site.travellaboratory.be.presentation.auth.dto.pwinquiry.PwInquiryVerificationResponse;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PwInquiryController {

    private final PwInquiryService pwInquiryService;

    @PostMapping("/auth/pw-inquiry/email")
    public ResponseEntity<PwInquiryEmailResponse> pwInquiryEmail(
        @RequestBody PwInquiryEmailRequest pwInquiryEmailRequest
    ) {
        return ResponseEntity.ok().body(pwInquiryService.pwInquiryEmail(pwInquiryEmailRequest));
    }

    @PostMapping("/auth/pw-inquiry/verification")
    public ResponseEntity<PwInquiryVerificationResponse> pwInquiryVerification(
        @RequestBody PwInquiryVerificationRequest pwInquiryVerificationRequest
    ) {
        return ResponseEntity.ok().body(pwInquiryService.pwInquiryVerification(pwInquiryVerificationRequest));
    }

    @PostMapping("/auth/pw-inquiry/renewal")
    public ResponseEntity<Void> pwInquiryRenewal(
        @RequestBody PwInquiryRenewalRequest pwInquiryRenewalRequest
    ) {
        pwInquiryService.pwInquiryRenewal(pwInquiryRenewalRequest);
        return ResponseEntity.ok().build();
    }
}

