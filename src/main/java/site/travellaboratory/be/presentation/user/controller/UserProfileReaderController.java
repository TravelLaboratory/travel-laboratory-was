package site.travellaboratory.be.presentation.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.application.user.UserProfileReaderService;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.presentation.user.dto.reader.UserProfileResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserProfileReaderController {

    private final UserProfileReaderService userProfileReaderService;

    @GetMapping("/profile/{id}")
    public ResponseEntity<UserProfileResponse> findMyProfile(
            @UserId final Long userId,
            @PathVariable(name = "id") final Long id
    ) {
        final UserProfileResponse userProfileResponse = userProfileReaderService.findByUserProfile(userId, id);
        return ResponseEntity.ok(userProfileResponse);
    }
}