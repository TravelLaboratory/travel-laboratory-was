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

    @GetMapping("/auth/reissue-token")
    public ResponseEntity<Void> refreshAccessToken(
        @RequestHeader("authorization-token") String accessToken,
        @RequestHeader("refresh-token") String refreshToken,
//        @CookieValue(value = "refresh-token") String refreshToken, // todo: 추후 cookie 변경시 주석 제거
        HttpServletResponse response
    ) {
        System.out.println("refreshToken = " + refreshToken);
        // 쿠키에서 리프레시 토큰 값 추출
        AccessTokenResponse accessTokenResponse = userAuthenticationService.reIssueAccessToken(accessToken,
            refreshToken);

        // AccessToken - authorization-token 헤더에 추가 (+ 만료기간)
        response.setHeader("authorization-token", accessTokenResponse.accessToken());
        response.setHeader("authorization-token-expired-at", accessTokenResponse.expiredAt());

        return ResponseEntity.ok().build();
    }
}

