package site.travellaboratory.be.user.presentation._auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.user.application._auth.UserAuthenticationService;
import site.travellaboratory.be.user.presentation._auth.response.userauthentication.LoginResponse;
import site.travellaboratory.be.user.presentation._auth.response.userauthentication.LoginRequest;
import site.travellaboratory.be.user.application._auth.command.LoginCommand;
import site.travellaboratory.be.user.presentation._auth.response.userauthentication.AccessTokenResponse;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserAuthenticationController {

    private final UserAuthenticationService userAuthenticationService;

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(
        @RequestBody LoginRequest loginRequest,
        HttpServletResponse response
    ) {
        LoginCommand result = userAuthenticationService.login(loginRequest);

        response.setHeader("authorization-token", result.accessToken());
        response.setHeader("authorization-token-expired-at", result.expiredAt());
        response.setHeader("refresh-token", result.refreshToken());

        return ResponseEntity.ok(LoginResponse.from(result.userId(), result.nickname(),
            result.profileImgUrl()));
    }

    @GetMapping("/auth/reissue-token")
    public ResponseEntity<Void> refreshAccessToken(
        @RequestHeader("authorization-token") String accessToken,
        @RequestHeader("refresh-token") String refreshToken,
        HttpServletResponse response
    ) {
        System.out.println("refreshToken = " + refreshToken);
        // 쿠키에서 리프레시 토큰 값 추출
        AccessTokenResponse accessTokenResponse = userAuthenticationService.reIssueAccessToken(accessToken, refreshToken);

        response.setHeader("authorization-token", accessTokenResponse.accessToken());
        response.setHeader("authorization-token-expired-at", accessTokenResponse.expiredAt());

        return ResponseEntity.ok().build();
    }
}

