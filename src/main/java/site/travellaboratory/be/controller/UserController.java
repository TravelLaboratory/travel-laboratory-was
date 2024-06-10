package site.travellaboratory.be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.controller.dto.user.ProfileImgUpdateRequest;
import site.travellaboratory.be.controller.dto.user.ProfileImgUpdateResponse;
import site.travellaboratory.be.controller.dto.user.UserProfileResponse;
import site.travellaboratory.be.controller.dto.user.UserProfileUpdateRequest;
import site.travellaboratory.be.controller.dto.user.UserProfileUpdateResponse;
import site.travellaboratory.be.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user/{userId}/profile")
    public ResponseEntity<UserProfileResponse> findProfile(@PathVariable final Long userId) {
        final UserProfileResponse userProfileResponse = userService.findByUserProfile(userId);
        return ResponseEntity.ok(userProfileResponse);
    }

    @PutMapping("/user/{userId}/profile")
    public ResponseEntity<UserProfileUpdateResponse> updateProfile(
            @RequestBody UserProfileUpdateRequest userProfileUpdateRequest,
            @PathVariable final Long userId) {
        final UserProfileUpdateResponse userProfileUpdateResponse = userService.updateProfile(userProfileUpdateRequest,
                userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userProfileUpdateResponse);
    }

    @PatchMapping("/api/v1/user/{userId}/profile/image")
    public ResponseEntity<ProfileImgUpdateResponse> updateProfileImage(
            @RequestBody ProfileImgUpdateRequest profileImgUpdateRequest,
            @PathVariable final Long userId
    ) {
        final ProfileImgUpdateResponse profileImgUpdateResponse = userService.updateProfileImage(
                profileImgUpdateRequest, userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(profileImgUpdateResponse);
    }
}
