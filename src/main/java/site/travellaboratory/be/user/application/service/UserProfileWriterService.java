package site.travellaboratory.be.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.travellaboratory.be.common.error.ErrorCodes;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.infrastructure.aws.S3FileUploader;
import site.travellaboratory.be.user.application.port.UserRepository;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain.enums.UserStatus;
import site.travellaboratory.be.user.domain.request.UserProfileInfoUpdateRequest;

@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileWriterService {

    private final UserRepository userRepository;
    private final S3FileUploader s3FileUploader;

    @Transactional
    public User updateProfileInfo(Long userId, UserProfileInfoUpdateRequest request) {

        User user = getUserById(userId);
        // 닉네임 중복 체크
        userRepository.findByNickname(request.nickname()).ifPresent(it -> {
            throw new BeApplicationException(ErrorCodes.PROFILE_DUPLICATED_NICK_NAME,
                HttpStatus.CONFLICT);
        });

        return userRepository.save(user.withProfileInfo(request));
    }

    public User updateProfileImg(Long userId, MultipartFile file) {
        User user = getUserById(userId);
        final String uploadImgUrl = s3FileUploader.uploadFiles(file);
        return userRepository.save(user.withProfileImg(uploadImgUrl));
    }

    private User getUserById(Long userId) {
        return userRepository.getByIdAndStatus(userId, UserStatus.ACTIVE);
    }
}
