package site.travellaboratory.be.common.application;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import site.travellaboratory.be.common.error.ErrorCodes;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.infrastructure.aws.S3Uploader;
import site.travellaboratory.be.common.infrastructure.image.ImageProcessor;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

    private static final int PROFILE_IMAGE_WIDTH = 250;
    private static final int PROFILE_IMAGE_HEIGHT = 250;
    private static final double IMAGE_QUALITY = 0.8;
    private static final int COVER_IMAGE_WIDTH = 1920;
    private static final int COVER_IMAGE_HEIGHT = 560;

    private static final String PROFILE_IMAGE_PREFIX = "profile";
    private static final String COVER_IMAGE_PREFIX = "article_background";
    private static final String PROFILE_FOLDER = "profiles";
    private static final String COVER_FOLDER = "article_backgrounds";
    private static final String DATE_TIME_FORMAT = "yyyyMMddHHmmss";
    private static final Set<String> ALLOWED_FILE_TYPES = Set.of(".jpg", ".jpeg", ".png", ".gif");


    private final ImageProcessor imageProcessor;
    private final S3Uploader s3Uploader;

    public String uploadProfileImage(MultipartFile profileImage) {
        verifyFile(profileImage);
        ByteArrayOutputStream processedImage;
        try {
            processedImage = imageProcessor.resizeAndCompressImage(
                profileImage, PROFILE_IMAGE_WIDTH, PROFILE_IMAGE_HEIGHT, IMAGE_QUALITY);
        } catch (IOException e) {
            throw new BeApplicationException(ErrorCodes.IMAGE_RESIZING_AND_COMPRESS_FAILED,
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String uniqueFileName = generateUniqueFileName(profileImage.getOriginalFilename(), PROFILE_IMAGE_PREFIX);
        return s3Uploader.uploadImageFile(uniqueFileName, processedImage, PROFILE_FOLDER);
    }

    public String uploadCoverImage(MultipartFile coverImage) {
        verifyFile(coverImage);
        ByteArrayOutputStream processedImage;
        try {
            processedImage = imageProcessor.resizeAndCompressImage(
                coverImage, COVER_IMAGE_WIDTH, COVER_IMAGE_HEIGHT, IMAGE_QUALITY);
        } catch (IOException e) {
            throw new BeApplicationException(ErrorCodes.IMAGE_RESIZING_AND_COMPRESS_FAILED,
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String uniqueFileName = generateUniqueFileName(coverImage.getOriginalFilename(), COVER_IMAGE_PREFIX);
        return s3Uploader.uploadImageFile(uniqueFileName, processedImage, COVER_FOLDER);
    }

    private String generateUniqueFileName(String originalFileName, String filePrefix) {
        int lastIndex = originalFileName.lastIndexOf('.');
        if (lastIndex <= 0 || lastIndex == originalFileName.length() - 1) {
            throw new BeApplicationException(ErrorCodes.INVALID_FILE_FORMAT, HttpStatus.BAD_REQUEST);
        }

        String extension = originalFileName.substring(lastIndex);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
        String uuid = UUID.randomUUID().toString();
        return filePrefix + "_" + timestamp + "_" + uuid + extension;
    }

    private void verifyFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BeApplicationException(ErrorCodes.FILE_IS_EMPTY, HttpStatus.BAD_REQUEST);
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || file.getOriginalFilename().isEmpty()) {
            throw new BeApplicationException(ErrorCodes.FILE_NAME_EMPTY, HttpStatus.BAD_REQUEST);
        }
        if (file.getSize() == 0) {
            throw new BeApplicationException(ErrorCodes.FILE_SIZE_ZERO, HttpStatus.BAD_REQUEST);
        }

        String extension = getFileExtension(originalFilename);
        if (!ALLOWED_FILE_TYPES.contains(extension.toLowerCase())) {
            throw new BeApplicationException(ErrorCodes.INVALID_FILE_FORMAT, HttpStatus.BAD_REQUEST);
        }
    }
    private String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex <= 0 || lastIndex == fileName.length() - 1) {
            throw new BeApplicationException(ErrorCodes.NOT_EXIST_FILE_FORMAT, HttpStatus.BAD_REQUEST);
        }
        return fileName.substring(lastIndex);
    }
}
