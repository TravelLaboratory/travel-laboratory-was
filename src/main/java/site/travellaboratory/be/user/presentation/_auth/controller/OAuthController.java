package site.travellaboratory.be.user.presentation._auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.user.presentation._auth.response.userauthentication.LoginResponse;
import site.travellaboratory.be.user.application._auth.command.LoginCommand;
import site.travellaboratory.be.user.domain._auth.request.OAuthJoinRequest;
import site.travellaboratory.be.user.application._auth.OAuthService;

@RestController
@RequestMapping("/api/v1/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oAuthService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> join(
        @RequestBody OAuthJoinRequest oAuthJoinRequest,
        HttpServletResponse response
    ) {
        LoginCommand result = oAuthService.kakaoLogin(oAuthJoinRequest);

        response.setHeader("authorization-token", result.accessToken());
        response.setHeader("authorization-token-expired-at", result.expiredAt());
        response.setHeader("refresh-token", result.refreshToken());
        return ResponseEntity.ok(LoginResponse.from(result.userId(), result.nickname(),
            result.profileImgUrl()));
    }
}