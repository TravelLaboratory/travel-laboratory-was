package site.travellaboratory.be.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.controller.user.dto.ProfileImgUpdateRequest;
import site.travellaboratory.be.controller.user.dto.ProfileImgUpdateResponse;
import site.travellaboratory.be.controller.user.dto.UserProfileResponse;
import site.travellaboratory.be.controller.user.dto.UserProfileUpdateRequest;
import site.travellaboratory.be.controller.user.dto.UserProfileUpdateResponse;
import site.travellaboratory.be.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> findMyProfile(@UserId final Long userId) {
        final UserProfileResponse userProfileResponse = userService.findByUserProfile(userId);
        return ResponseEntity.ok(userProfileResponse);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileUpdateResponse> updateMyProfile(
            @RequestBody UserProfileUpdateRequest userProfileUpdateRequest,
            @UserId final Long userId) {
        final UserProfileUpdateResponse userProfileUpdateResponse = userService.updateProfile(userProfileUpdateRequest,
                userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userProfileUpdateResponse);
    }

    @PutMapping("/profile/image")
    public ResponseEntity<ProfileImgUpdateResponse> updateMyProfileImage(
            @RequestBody ProfileImgUpdateRequest profileImgUpdateRequest,
            @UserId final Long userId
    ) {
        final ProfileImgUpdateResponse profileImgUpdateResponse = userService.updateProfileImage(
                profileImgUpdateRequest, userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(profileImgUpdateResponse);
    }
}
