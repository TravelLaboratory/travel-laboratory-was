package site.travellaboratory.be.user.presentation._auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.user.application._auth.UserUnregistrationService;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.user.presentation._auth.response.userunregistration.UserUnregisterResponse;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserUnregistrationController {

    private final UserUnregistrationService userUnregistrationService;

    @PatchMapping("/auth/signout")
    public ResponseEntity<UserUnregisterResponse> unregister(
        @UserId final Long userId
    ) {
        UserUnregisterResponse userUnRegisterResponse = userUnregistrationService.unregister(userId);
        return ResponseEntity.ok(userUnRegisterResponse);
    }
}

