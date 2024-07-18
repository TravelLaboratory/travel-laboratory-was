package site.travellaboratory.be.user.presentation._auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.user.application._auth.UserVerificationService;
import site.travellaboratory.be.user.domain._auth.request.UserNicknameRequest;
import site.travellaboratory.be.user.presentation._auth.response.userverification.UserNicknameResponse;
import site.travellaboratory.be.user.domain._auth.request.UsernameRequest;
import site.travellaboratory.be.user.presentation._auth.response.userverification.UsernameResponse;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserVerificationController {

    private final UserVerificationService userVerificationService;

    @PostMapping("/auth/nickname")
    public ResponseEntity<UserNicknameResponse> checkNicknameDuplicate(
        @RequestBody UserNicknameRequest userNicknameRequest
    ) {
        Boolean result = userVerificationService.isNicknameAvailable(
            userNicknameRequest);
        return ResponseEntity.ok(UserNicknameResponse.from(result));
    }

    @PostMapping("/auth/username")
    public ResponseEntity<UsernameResponse> checkUsernameDuplicate(
        @RequestBody UsernameRequest usernameRequest
    ) {
        Boolean result = userVerificationService.isUsernameAvailable(usernameRequest);
        return ResponseEntity.ok(UsernameResponse.from(result));
    }
}

