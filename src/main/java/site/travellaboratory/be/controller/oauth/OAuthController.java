package site.travellaboratory.be.controller.oauth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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

        response.setHeader("refresh-token", authTokenResponse.refreshToken());
        return ResponseEntity.ok().build();
    }
}
