package site.travellaboratory.be.controller.auth;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.controller.auth.dto.UserInfoResponse;
import site.travellaboratory.be.controller.auth.dto.UserJoinRequest;
import site.travellaboratory.be.controller.auth.dto.UserJoinResponse;
import site.travellaboratory.be.controller.auth.dto.UserLoginRequest;
import site.travellaboratory.be.controller.auth.dto.UserLoginResponse;
import site.travellaboratory.be.controller.auth.dto.UserNicknameRequest;
import site.travellaboratory.be.controller.auth.dto.UserNicknameResponse;
import site.travellaboratory.be.controller.auth.dto.UsernameRequest;
import site.travellaboratory.be.controller.auth.dto.UsernameResponse;
import site.travellaboratory.be.controller.auth.dto.pw.PwInquiryEmailRequest;
import site.travellaboratory.be.controller.auth.dto.pw.PwInquiryEmailResponse;
import site.travellaboratory.be.controller.auth.dto.pw.PwInquiryRenewalRequest;
import site.travellaboratory.be.controller.auth.dto.pw.PwInquiryVerificationRequest;
import site.travellaboratory.be.controller.auth.dto.pw.PwInquiryVerificationResponse;
import site.travellaboratory.be.controller.jwt.dto.AccessTokenResponse;
import site.travellaboratory.be.service.UserAuthService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserAuthController {

    private final UserAuthService userAuthService;

    @PostMapping("/join")
    public ResponseEntity<UserJoinResponse> join(
        @Valid @RequestBody UserJoinRequest userJoinRequest
    ) {
        return ResponseEntity.ok().body(userAuthService.join(userJoinRequest));
    }

    @PostMapping("/nickname")
    public ResponseEntity<UserNicknameResponse> checkNicknameDuplicate(
        @RequestBody UserNicknameRequest userNicknameRequest
    ) {
        return ResponseEntity.ok(userAuthService.isNicknameAvailable(userNicknameRequest));
    }

    @PostMapping("/username")
    public ResponseEntity<UsernameResponse> checkUsernameDuplicate(
        @RequestBody UsernameRequest usernameRequest
    ) {
        return ResponseEntity.ok(userAuthService.isUsernameAvailable(usernameRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<UserInfoResponse> login(
        @RequestBody UserLoginRequest userLoginRequest,
        HttpServletResponse response
    ) {
        UserLoginResponse userLoginResponse = userAuthService.login(userLoginRequest);

        // AccessToken - authorization-token 헤더에 추가 (+만료기간까지)
        response.setHeader("authorization-token", userLoginResponse.authTokenResponse().accessToken());
        response.setHeader("authorization-token-expired-at", userLoginResponse.authTokenResponse().expiredAt());

/*
        // RefreshToken - refresh-token 쿠키에 추가
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh-token", authTokenResponse.refreshToken())
            .httpOnly(true)
            .path("/api/v1/auth/reissue-token")
            .maxAge(14 * 24 * 60 * 60) // 2주
            .secure(true)
            .sameSite(NONE.attributeValue())
//            .sameSite(STRICT.attributeValue())
//            .domain("travel-laboratory.site")
            .build();
        response.setHeader("Set-Cookie", refreshTokenCookie.toString()); // 일단 refresh-token만 헤더로 넣을 것이기에 setHeader로 설정*/

        // todo: 추후 cookie 변경 시 제거
        response.setHeader("refresh-token", userLoginResponse.authTokenResponse().refreshToken());
        return ResponseEntity.ok(userLoginResponse.userInfoResponse());
    }

    @GetMapping("/reissue-token")
    public ResponseEntity<Void> refreshAccessToken(
        @RequestHeader("authorization-token") String accessToken,
        @RequestHeader("refresh-token") String refreshToken,
//        @CookieValue(value = "refresh-token") String refreshToken, // todo: 추후 cookie 변경시 주석 제거
        HttpServletResponse response
    ) {
        System.out.println("refreshToken = " + refreshToken);
        // 쿠키에서 리프레시 토큰 값 추출
        AccessTokenResponse accessTokenResponse = userAuthService.reIssueAccessToken(accessToken,
            refreshToken);

        // AccessToken - authorization-token 헤더에 추가 (+ 만료기간)
        response.setHeader("authorization-token", accessTokenResponse.accessToken());
        response.setHeader("authorization-token-expired-at", accessTokenResponse.expiredAt());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/pw-inquiry/email")
    public ResponseEntity<PwInquiryEmailResponse> pwInquiryEmail(
        @RequestBody PwInquiryEmailRequest pwInquiryEmailRequest
    ) {
        return ResponseEntity.ok().body(userAuthService.pwInquiryEmail(pwInquiryEmailRequest));
    }

    @PostMapping("/pw-inquiry/verification")
    public ResponseEntity<PwInquiryVerificationResponse> pwInquiryVerification(
        @RequestBody PwInquiryVerificationRequest pwInquiryVerificationRequest
    ) {
        return ResponseEntity.ok().body(userAuthService.pwInquiryVerification(pwInquiryVerificationRequest));
    }

    @PostMapping("/pw-inquiry/renewal")
    public ResponseEntity<Void> pwInquiryRenewal(
        @RequestBody PwInquiryRenewalRequest pwInquiryRenewalRequest
    ) {
        userAuthService.pwInquiryRenewal(pwInquiryRenewalRequest);
        return ResponseEntity.ok().build();
    }
}

