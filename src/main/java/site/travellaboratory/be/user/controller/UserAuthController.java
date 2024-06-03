package site.travellaboratory.be.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.common.response.ApiResponse;
import site.travellaboratory.be.user.controller.dto.UserJoinRequest;
import site.travellaboratory.be.user.controller.dto.UserJoinResponse;
import site.travellaboratory.be.user.service.UserAuthService;
import site.travellaboratory.be.user.service.domain.User;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserAuthController {

    private final UserAuthService userAuthService;

    @PostMapping("/join")
    public ApiResponse<UserJoinResponse> join(
        @RequestBody UserJoinRequest request
    ) {
        User user = userAuthService.join(request.userName(), request.password(), request.nickName());
        return ApiResponse.success(UserJoinResponse.fromDomain(user));
    }

}
