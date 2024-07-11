package site.travellaboratory.be.presentation.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.application.auth.UserVerificationService;
import site.travellaboratory.be.presentation.auth.dto.userverification.UserNicknameRequest;
import site.travellaboratory.be.presentation.auth.dto.userverification.UserNicknameResponse;
import site.travellaboratory.be.presentation.auth.dto.userverification.UsernameRequest;
import site.travellaboratory.be.presentation.auth.dto.userverification.UsernameResponse;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserVerificationController {

    private final UserVerificationService userVerificationService;

    @PostMapping("/auth/nickname")
    public ResponseEntity<UserNicknameResponse> checkNicknameDuplicate(
        @RequestBody UserNicknameRequest userNicknameRequest
    ) {
        return ResponseEntity.ok(userVerificationService.isNicknameAvailable(userNicknameRequest));
    }

    @PostMapping("/auth/username")
    public ResponseEntity<UsernameResponse> checkUsernameDuplicate(
        @RequestBody UsernameRequest usernameRequest
    ) {
        return ResponseEntity.ok(userVerificationService.isUsernameAvailable(usernameRequest));
    }
}

