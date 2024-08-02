package site.travellaboratory.be.user.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.common.presentation.response.ApiResponse;
import site.travellaboratory.be.user.application.service.UserProfileReaderService;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.user.presentation.response.reader.UserProfileResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserProfileReaderController {

    private final UserProfileReaderService userProfileReaderService;

    @GetMapping("/profile/{id}")
    public ResponseEntity<ApiResponse<UserProfileResponse>> findMyProfile(
            @UserId final Long userId,
            @PathVariable(name = "id") final Long id
    ) {
        final UserProfileResponse userProfileResponse = userProfileReaderService.findByUserProfile(userId, id);
        return ResponseEntity.ok(ApiResponse.OK(userProfileResponse));
    }
}
