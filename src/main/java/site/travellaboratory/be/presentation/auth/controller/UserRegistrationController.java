package site.travellaboratory.be.presentation.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.application.auth.UserRegistrationService;
import site.travellaboratory.be.domain.user.user.User;
import site.travellaboratory.be.presentation.auth.dto.userregistration.UserJoinRequest;
import site.travellaboratory.be.presentation.auth.dto.userregistration.UserRegisterResponse;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserRegistrationController {

    private final UserRegistrationService userRegistrationService;

    @PostMapping("/auth/join")
    public ResponseEntity<UserRegisterResponse> register(
        @Valid @RequestBody UserJoinRequest userJoinRequest
    ) {
        User result = userRegistrationService.register(userJoinRequest);
        return ResponseEntity.ok().body(UserRegisterResponse.from(result));
    }

}

