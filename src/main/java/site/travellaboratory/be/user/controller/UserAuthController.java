package site.travellaboratory.be.user.controller;

import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<AuthTokenResponse> login(
        @RequestBody UserLoginRequest userLoginRequest
    ) {
        // todo : 헤더로 바꿔서 보내기
        // todo : refresh는 set-cookie로 감싸서 보내기
        return ResponseEntity.ok().body(userAuthService.login(userLoginRequest));
    }

    @GetMapping("/me")
    public Long me(
        @UserId Long userId
    ) {
        System.out.println("user.getId() = " + userId);
        userAuthService.test(userId);
        return userId;

    }
    

    @PostMapping("/refresh-token")
    public ResponseEntity<AccessTokenResponse> refreshAccessToken(
        @RequestHeader("authorization-token") String accessToken,
        @RequestHeader("refresh-token") String refreshToken
    ) {
        // todo: header로 보내기
        return ResponseEntity.ok().body(userAuthService.reIssueAccessToken(accessToken,
            refreshToken));
    }
}

