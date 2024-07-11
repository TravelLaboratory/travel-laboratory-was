package site.travellaboratory.be.presentation.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import site.travellaboratory.be.application.user.UserProfileService;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.presentation.user.dto.UserProfileResponse;
import site.travellaboratory.be.presentation.user.dto.UserProfileUpdateRequest;
import site.travellaboratory.be.presentation.user.dto.UserProfileUpdateResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/profile/{id}")
    public ResponseEntity<UserProfileResponse> findMyProfile(
            @UserId final Long userId,
            @PathVariable(name = "id") final Long id
    ) {
        final UserProfileResponse userProfileResponse = userProfileService.findByUserProfile(userId, id);
        return ResponseEntity.ok(userProfileResponse);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileUpdateResponse> updateMyProfile(
            @RequestPart(value = "file", required = false) final MultipartFile file,
            @RequestPart("profile") final UserProfileUpdateRequest userProfileUpdateRequest,
            @UserId final Long userId) {
        final UserProfileUpdateResponse userProfileUpdateResponse = userProfileService.updateProfile(file,
                userProfileUpdateRequest,
                userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userProfileUpdateResponse);
    }
}
