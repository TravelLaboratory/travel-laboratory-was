package site.travellaboratory.be.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.common.annotation.AuthenticatedUser;
import site.travellaboratory.be.common.response.ApiResponse;
import site.travellaboratory.be.jwt.dto.JwtTokenResponse;
import site.travellaboratory.be.jwt.dto.RefreshAccessTokenResponse;
import site.travellaboratory.be.jwt.model.JwtToken;
import site.travellaboratory.be.jwt.model.Token;
import site.travellaboratory.be.jwt.service.TokenService;
import site.travellaboratory.be.user.controller.dto.UserJoinRequest;
import site.travellaboratory.be.user.controller.dto.UserJoinResponse;
import site.travellaboratory.be.user.controller.dto.UserLoginRequest;
import site.travellaboratory.be.user.service.UserAuthService;
import site.travellaboratory.be.user.service.domain.User;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserAuthController {

    private final UserAuthService userAuthService;
    private final TokenService tokenService;

    @PostMapping("/join")
    public ApiResponse<UserJoinResponse> join(
        @RequestBody UserJoinRequest request
    ) {
        User user = userAuthService.join(request.userName(), request.password(),
            request.nickName());
        return ApiResponse.success(UserJoinResponse.fromDomain(user));
    }

    @PostMapping("/login")
    public ApiResponse<JwtTokenResponse> login(
        @RequestBody UserLoginRequest request
    ) {
        JwtToken jwtToken = userAuthService.login(request.userName(), request.password());
        return ApiResponse.success(JwtTokenResponse.fromDomain(jwtToken));
    }

    @GetMapping("/me")
    public User me(
        @AuthenticatedUser User user
    ) {
        System.out.println("user.getId() = " + user.getId());
        System.out.println("user.getUserName() = " + user.getUserName());
        System.out.println("user.getNickName() = " + user.getNickName());
        return user;
    }
    

    @PostMapping("/refresh-token")
    public ApiResponse<RefreshAccessTokenResponse> refreshAccessToken(
        @RequestHeader("authorization-token") String accessToken,
        @RequestHeader("refresh-token") String refreshToken
    ) {
        Token newAccessToken = tokenService.refreshAccessToken(accessToken, refreshToken);
        return ApiResponse.success(RefreshAccessTokenResponse.fromDomain(newAccessToken));
    }
}

