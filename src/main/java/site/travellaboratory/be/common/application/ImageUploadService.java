package site.travellaboratory.be.common.application;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private final ImageProcessor imageProcessor;
    private final S3Uploader s3Uploader;

    public String uploadProfileImage(MultipartFile file) {
        verifyFile(file);
        ByteArrayOutputStream processedImage;
        try {
            processedImage = imageProcessor.resizeAndCompressProfileImage(file);
        } catch (IOException e) {
            throw new BeApplicationException(ErrorCodes.IMAGE_RESIZING_AND_COMPRESS_FAILED,
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String uniqueFileName = generateUniqueFileName(file.getOriginalFilename(), "profile");
        return s3Uploader.uploadImageFile(uniqueFileName, processedImage, "profiles");
    }

    public String uploadBackgroundImage(MultipartFile file) {
        verifyFile(file);
        ByteArrayOutputStream processedImage;
        try {
            processedImage = imageProcessor.resizeAndCompressBackgroundImage(file);
        } catch (IOException e) {
            throw new BeApplicationException(ErrorCodes.IMAGE_RESIZING_AND_COMPRESS_FAILED,
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String uniqueFileName = generateUniqueFileName(file.getOriginalFilename(),
            "article_background");
        return s3Uploader.uploadImageFile(uniqueFileName, processedImage, "article_backgrounds");
    }

    private String generateUniqueFileName(String originalFileName, String filePrefix) {
        // 확장자 추출
        int lastIndex = originalFileName.lastIndexOf('.');

        // 확장자가 없을 경우 에러 반환
        if (lastIndex <= 0 || lastIndex == originalFileName.length() - 1) {
            throw new BeApplicationException(ErrorCodes.INVALID_FILE_FORMAT,
                HttpStatus.BAD_REQUEST);
        }

        String extension = originalFileName.substring(lastIndex);
        // 현재 날짜와 시간으로 생성
        String timestamp = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        // UUID 생성
        String uuid = UUID.randomUUID().toString();
        // 고유한 파일명 생성
        return filePrefix + "_" + timestamp + "_" + uuid + extension;
    }

    private void verifyFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BeApplicationException(ErrorCodes.FILE_IS_EMPTY, HttpStatus.BAD_REQUEST);
        }

        if (file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
            throw new BeApplicationException(ErrorCodes.FILE_NAME_EMPTY, HttpStatus.BAD_REQUEST);
        }

        if (file.getSize() == 0) {
            throw new BeApplicationException(ErrorCodes.FILE_SIZE_ZERO, HttpStatus.BAD_REQUEST);
        }
    }
}
