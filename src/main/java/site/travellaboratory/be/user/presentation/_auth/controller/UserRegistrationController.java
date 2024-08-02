package site.travellaboratory.be.user.presentation._auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.common.presentation.response.ApiResponse;
import site.travellaboratory.be.user.application._auth.UserRegistrationService;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain._auth.request.UserJoinRequest;
import site.travellaboratory.be.user.presentation._auth.response.userregistration.UserRegisterResponse;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserRegistrationController {

    private final UserRegistrationService userRegistrationService;

    @PostMapping("/auth/join")
    public ResponseEntity<ApiResponse<UserRegisterResponse>> register(
        @Valid @RequestBody UserJoinRequest userJoinRequest
    ) {
        User result = userRegistrationService.register(userJoinRequest);
        return ResponseEntity.ok().body(ApiResponse.OK(UserRegisterResponse.from(result)));
    }
}

