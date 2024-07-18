package site.travellaboratory.be.user.application;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.common.infrastructure.aws.S3FileUploader;
import site.travellaboratory.be.user.infrastructure.persistence.repository.UserJpaRepository;
import site.travellaboratory.be.user.infrastructure.persistence.entity.UserEntity;
import site.travellaboratory.be.user.domain.enums.UserStatus;
import site.travellaboratory.be.user.domain.request.UserProfileUpdateRequest;
import site.travellaboratory.be.user.presentation.response.writer.UserProfileUpdateResponse;

@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileWriterService {

    private final UserJpaRepository userJpaRepository;
    private final S3FileUploader s3FileUploader;

    public UserProfileUpdateResponse updateProfile(
            final MultipartFile file,
            final UserProfileUpdateRequest userProfileUpdateRequest,
            final Long userId
    ) {
        final UserEntity userEntity = userJpaRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND,
                        HttpStatus.NOT_FOUND));

        String url = userEntity.getProfileImgUrl();

        if (file != null && !file.isEmpty()) {
            url = s3FileUploader.uploadFiles(file);
        }

        userEntity.update(userProfileUpdateRequest.nickname(), url, userProfileUpdateRequest.introduce());

        return new UserProfileUpdateResponse(userEntity.getNickname(),
                userEntity.getProfileImgUrl(),
                userEntity.getIntroduce());
    }
}
