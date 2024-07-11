package site.travellaboratory.be.presentation.oauth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.presentation.auth.dto.userauthentication.UserInfoResponse;
import site.travellaboratory.be.presentation.auth.dto.userauthentication.UserLoginResponse;
import site.travellaboratory.be.presentation.oauth.dto.OAuthJoinRequest;
import site.travellaboratory.be.application.auth.OAuthService;

@RestController
@RequestMapping("/api/v1/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oAuthService;

    @PostMapping("/login")
    public ResponseEntity<UserInfoResponse> join(
            @RequestBody OAuthJoinRequest oAuthJoinRequest,
            HttpServletResponse response
    ) {
        UserLoginResponse userLoginResponse = oAuthService.login(oAuthJoinRequest);

        // AccessToken - authorization-token 헤더에 추가 (+만료기간까지)
        response.setHeader("authorization-token", userLoginResponse.authTokenResponse().accessToken());
        response.setHeader("authorization-token-expired-at", userLoginResponse.authTokenResponse().expiredAt());

        response.setHeader("refresh-token", userLoginResponse.authTokenResponse().refreshToken());
        return ResponseEntity.ok(userLoginResponse.userInfoResponse());
    }
}
