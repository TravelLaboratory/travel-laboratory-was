package site.travellaboratory.be.user.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.common.presentation.response.ApiResponse;
import site.travellaboratory.be.user.application.service.UserProfileWriterService;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain.request.UserProfileInfoUpdateRequest;
import site.travellaboratory.be.user.presentation.response.writer.UserProfileImgUpdateResponse;
import site.travellaboratory.be.user.presentation.response.writer.UserProfileInfoUpdateResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserProfileWriterController {

    private final UserProfileWriterService userProfileWriterService;

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserProfileInfoUpdateResponse>> updateProfileInfo(
        @RequestBody UserProfileInfoUpdateRequest userProfileInfoUpdateRequest,
        @UserId Long userId) {
        final User result = userProfileWriterService.updateProfileInfo(userId, userProfileInfoUpdateRequest);
        return ResponseEntity.ok(ApiResponse.OK(UserProfileInfoUpdateResponse.from(result)));
    }

    @PutMapping("/profile/img")
    public ResponseEntity<ApiResponse<UserProfileImgUpdateResponse>> updateProfileImg(
            @RequestPart(value = "file") final MultipartFile file,
            @UserId Long userId) {
        final User result = userProfileWriterService.updateProfileImg(userId, file);
        return ResponseEntity.ok(ApiResponse.OK(UserProfileImgUpdateResponse.from(result)));
    }
}
