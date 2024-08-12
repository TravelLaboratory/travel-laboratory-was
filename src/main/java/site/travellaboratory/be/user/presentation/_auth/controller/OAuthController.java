package site.travellaboratory.be.user.presentation._auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.common.presentation.response.ApiResponse;
import site.travellaboratory.be.user.application._auth.OAuthService;
import site.travellaboratory.be.user.application._auth.command.LoginCommand;
import site.travellaboratory.be.user.domain._auth.request.OAuthJoinRequest;
import site.travellaboratory.be.user.presentation._auth.response.userauthentication.LoginResponse;

@RestController
@RequestMapping("/api/v1/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oAuthService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> join(
        @RequestBody OAuthJoinRequest oAuthJoinRequest,
        HttpServletResponse response
    ) {
        LoginCommand result = oAuthService.kakaoLogin(oAuthJoinRequest);

        response.setHeader("authorization-token", result.accessToken().getToken());
        response.setHeader("authorization-token-expired-at", result.accessToken().getExpiredAt().toString());
        response.setHeader("refresh-token", result.refreshToken().getToken());
        return ApiResponse.OK(LoginResponse.from(result.userId(), result.nickname(),
            result.profileImgUrl()));
    }
}
