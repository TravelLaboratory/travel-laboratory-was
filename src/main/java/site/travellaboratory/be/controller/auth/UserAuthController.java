package site.travellaboratory.be.controller.auth;

import static org.springframework.boot.web.server.Cookie.SameSite.NONE;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.controller.auth.dto.UserJoinRequest;
import site.travellaboratory.be.controller.auth.dto.UserJoinResponse;
import site.travellaboratory.be.controller.jwt.dto.AccessTokenResponse;
import site.travellaboratory.be.controller.jwt.dto.AuthTokenResponse;
import site.travellaboratory.be.controller.auth.dto.UserLoginRequest;
import site.travellaboratory.be.service.UserAuthService;

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
        @RequestBody UserLoginRequest userLoginRequest,
        HttpServletResponse response
    ) {
        AuthTokenResponse authTokenResponse = userAuthService.login(userLoginRequest);

        // AccessToken - authorization-token 헤더에 추가
        response.setHeader("authorization-token", authTokenResponse.accessToken());

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
        response.setHeader("Set-Cookie", refreshTokenCookie.toString()); // 일단 refresh-token만 헤더로 넣을 것이기에 setHeader로 설정
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reissue-token")
    public ResponseEntity<Void> refreshAccessToken(
        @RequestHeader("authorization-token") String accessToken,
        @CookieValue(value = "refresh-token") String refreshToken,
        HttpServletResponse response
    ) {
        System.out.println("refreshToken = " + refreshToken);
        // 쿠키에서 리프레시 토큰 값 추출
        AccessTokenResponse accessTokenResponse = userAuthService.reIssueAccessToken(accessToken,
            refreshToken);

        // AccessToken - authorization-token 헤더에 추가
        response.setHeader("authorization-token", accessTokenResponse.accessToken());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public Long me(
        @UserId Long userId
    ) {
        System.out.println("user.getId() = " + userId);
        userAuthService.test(userId);
        return userId;

    }
}

