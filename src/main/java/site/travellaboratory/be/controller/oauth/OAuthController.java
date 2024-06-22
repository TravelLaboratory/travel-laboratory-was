package site.travellaboratory.be.controller.oauth;

import static org.springframework.boot.web.server.Cookie.SameSite.NONE;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.controller.jwt.dto.AuthTokenResponse;
import site.travellaboratory.be.controller.oauth.dto.OAuthJoinRequest;
import site.travellaboratory.be.service.OAuthService;

@RestController
@RequestMapping("/api/v1/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oAuthService;

    @PostMapping("/login")
    public ResponseEntity<Void> join(
            @RequestBody OAuthJoinRequest oAuthJoinRequest,
            HttpServletResponse response
    ) {
        AuthTokenResponse authTokenResponse = oAuthService.login(oAuthJoinRequest);

        // AccessToken - authorization-token 헤더에 추가 (+만료기간까지)
        response.setHeader("authorization-token", authTokenResponse.accessToken());
        response.setHeader("authorization-token-expired-at", authTokenResponse.expiredAt());

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
}
