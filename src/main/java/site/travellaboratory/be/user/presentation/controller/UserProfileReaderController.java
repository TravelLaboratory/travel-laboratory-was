package site.travellaboratory.be.user.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.common.presentation.response.ApiResponse;
import site.travellaboratory.be.user.application.service.UserProfileReaderService;
import site.travellaboratory.be.user.presentation.response.reader.UserProfileResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserProfileReaderController {

    private final UserProfileReaderService userProfileReaderService;

    @GetMapping("users/{userId}/profile")
    public ApiResponse<UserProfileResponse> findMyProfile(
            @UserId final Long loginId,
            @PathVariable(name = "userId") final Long searchUserId
    ) {
        final UserProfileResponse userProfileResponse = userProfileReaderService.findByUserProfile(loginId, searchUserId);
        return ApiResponse.OK(userProfileResponse);
    }
}
