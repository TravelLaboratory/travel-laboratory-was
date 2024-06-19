package site.travellaboratory.be.controller.user;

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
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.controller.user.dto.UserProfileResponse;
import site.travellaboratory.be.controller.user.dto.UserProfileUpdateRequest;
import site.travellaboratory.be.controller.user.dto.UserProfileUpdateResponse;
import site.travellaboratory.be.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile/{id}")
    public ResponseEntity<UserProfileResponse> findMyProfile(
            @UserId final Long userId,
            @PathVariable(name = "id") final Long id
    ) {
        final UserProfileResponse userProfileResponse = userService.findByUserProfile(userId, id);
        return ResponseEntity.ok(userProfileResponse);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileUpdateResponse> updateMyProfile(
            @RequestPart("file") final MultipartFile file,
            @RequestPart("profile") final UserProfileUpdateRequest userProfileUpdateRequest,
            @UserId final Long userId) {
        final UserProfileUpdateResponse userProfileUpdateResponse = userService.updateProfile(file,
                userProfileUpdateRequest,
                userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userProfileUpdateResponse);
    }
}
