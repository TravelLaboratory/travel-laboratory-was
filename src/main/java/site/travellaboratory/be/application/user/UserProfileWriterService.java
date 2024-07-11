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
import site.travellaboratory.be.presentation.user.dto.writer.UserProfileUpdateRequest;
import site.travellaboratory.be.presentation.user.dto.writer.UserProfileUpdateResponse;

@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileWriterService {

    private final UserRepository userRepository;
    private final S3FileUploader s3FileUploader;

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

        user.update(userProfileUpdateRequest.nickname(), url, userProfileUpdateRequest.introduce());

        return new UserProfileUpdateResponse(user.getNickname(),
                user.getProfileImgUrl(),
                user.getIntroduce());
    }
}
