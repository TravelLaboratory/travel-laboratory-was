package site.travellaboratory.be.user.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.common.presentation.response.ApiResponse;
import site.travellaboratory.be.user.application.service.UserProfileWriterService;
import site.travellaboratory.be.user.domain.request.UserProfileUpdateRequest;
import site.travellaboratory.be.user.presentation.response.writer.UserProfileUpdateResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserProfileWriterController {

    private final UserProfileWriterService userProfileWriterService;

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserProfileUpdateResponse>> updateMyProfile(
            @RequestPart(value = "file", required = false) final MultipartFile file,
            @RequestPart("profile") final UserProfileUpdateRequest userProfileUpdateRequest,
            @UserId final Long userId) {
        final UserProfileUpdateResponse userProfileUpdateResponse = userProfileWriterService.updateProfile(file,
                userProfileUpdateRequest,
                userId);
        return ResponseEntity.ok(ApiResponse.OK(userProfileUpdateResponse));
    }
}
