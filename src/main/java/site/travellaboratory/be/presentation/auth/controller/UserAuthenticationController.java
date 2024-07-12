package site.travellaboratory.be.presentation.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.application.auth.UserAuthenticationService;
import site.travellaboratory.be.presentation.auth.dto.userauthentication.UserInfoResponse;
import site.travellaboratory.be.presentation.auth.dto.userauthentication.UserLoginRequest;
import site.travellaboratory.be.presentation.auth.dto.userauthentication.UserLoginResponse;
import site.travellaboratory.be.presentation.auth.dto.userauthentication.AccessTokenResponse;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserAuthenticationController {

    private final UserAuthenticationService userAuthenticationService;

    @PostMapping("/auth/login")
    public ResponseEntity<UserInfoResponse> login(
        @RequestBody UserLoginRequest userLoginRequest,
        HttpServletResponse response
    ) {
        UserLoginResponse userLoginResponse = userAuthenticationService.login(userLoginRequest);

        response.setHeader("authorization-token", userLoginResponse.authTokenResponse().accessToken());
        response.setHeader("authorization-token-expired-at", userLoginResponse.authTokenResponse().expiredAt());

        response.setHeader("refresh-token", userLoginResponse.authTokenResponse().refreshToken());
        return ResponseEntity.ok(userLoginResponse.userInfoResponse());
    }

    @GetMapping("/auth/reissue-token")
    public ResponseEntity<Void> refreshAccessToken(
        @RequestHeader("authorization-token") String accessToken,
        @RequestHeader("refresh-token") String refreshToken,
        HttpServletResponse response
    ) {
        System.out.println("refreshToken = " + refreshToken);
        // 쿠키에서 리프레시 토큰 값 추출
        AccessTokenResponse accessTokenResponse = userAuthenticationService.reIssueAccessToken(accessToken,
            refreshToken);

        response.setHeader("authorization-token", accessTokenResponse.accessToken());
        response.setHeader("authorization-token-expired-at", accessTokenResponse.expiredAt());

        return ResponseEntity.ok().build();
    }
}

