package site.travellaboratory.be.application.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.infrastructure.aws.S3FileUploader;
import site.travellaboratory.be.infrastructure.domains.user.UserRepository;
import site.travellaboratory.be.infrastructure.domains.user.entity.User;
import site.travellaboratory.be.infrastructure.domains.user.enums.UserStatus;
import site.travellaboratory.be.presentation.user.dto.UserProfileResponse;
import site.travellaboratory.be.presentation.user.dto.UserProfileUpdateRequest;
import site.travellaboratory.be.presentation.user.dto.UserProfileUpdateResponse;

@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileService {

    private final UserRepository userRepository;
    private final S3FileUploader s3FileUploader;

    public UserProfileResponse findByUserProfile(final Long userId, final Long id) {
        final User user = userRepository.findByIdAndStatus(id, UserStatus.ACTIVE)
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.AUTH_USER_NOT_FOUND,
                        HttpStatus.BAD_REQUEST)
                );

        final boolean isEditable = user.getId().equals(userId);

        return UserProfileResponse.from(user, isEditable);
    }

    public UserProfileUpdateResponse updateProfile(
            final MultipartFile file,
            final UserProfileUpdateRequest userProfileUpdateRequest,
            final Long userId
    ) {
        final User user = userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND,
                        HttpStatus.NOT_FOUND));

        String url = user.getProfileImgUrl();

        if (file != null && !file.isEmpty()) {
            url = s3FileUploader.uploadFiles(file);
        }

        user.update(userProfileUpdateRequest.nickname(),
                url, userProfileUpdateRequest.introduce());

        return new UserProfileUpdateResponse(user.getNickname(),
                user.getProfileImgUrl(),
                user.getIntroduce());
    }
}
