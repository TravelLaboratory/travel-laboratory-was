package site.travellaboratory.be.user.controller;

import static org.springframework.boot.web.server.Cookie.SameSite.NONE;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.jwt.dto.AccessTokenResponse;
import site.travellaboratory.be.jwt.dto.AuthTokenResponse;
import site.travellaboratory.be.user.controller.dto.UserJoinRequest;
import site.travellaboratory.be.user.controller.dto.UserJoinResponse;
import site.travellaboratory.be.user.controller.dto.UserLoginRequest;
import site.travellaboratory.be.user.service.UserAuthService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserAuthController {

    private final UserAuthService userAuthService;

    @PostMapping("/join")
    public ResponseEntity<UserJoinResponse> join(
        @RequestBody UserJoinRequest userJoinRequest
    ) {
        return ResponseEntity.ok().body(userAuthService.join(userJoinRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
        @RequestBody UserLoginRequest userLoginRequest, HttpServletResponse response
    ) {
        AuthTokenResponse authTokenResponse = userAuthService.login(userLoginRequest);

        // AccessToken - authorization-token 헤더에 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization-token", authTokenResponse.accessToken());

        // RefreshToken - refresh-token 쿠키에 추가
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh-token", authTokenResponse.refreshToken())
            .httpOnly(true)
            .path("/api/*/auth/reissue-token")
            .maxAge(14 * 24 * 60 * 60) // 2주
            .secure(true)
            .sameSite(NONE.attributeValue())
            .build();
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
        return ResponseEntity.ok().headers(headers).build();
    }

    @GetMapping("/me")
    public Long me(
        @UserId Long userId
    ) {
        System.out.println("user.getId() = " + userId);
        userAuthService.test(userId);
        return userId;

    }
    

    @PostMapping("/reissue-token")
    public ResponseEntity<AccessTokenResponse> refreshAccessToken(
        @RequestHeader("authorization-token") String accessToken,
        @RequestHeader("refresh-token") String refreshToken
    ) {
        // todo: header로 보내기
        return ResponseEntity.ok().body(userAuthService.reIssueAccessToken(accessToken,
            refreshToken));
    }
}

