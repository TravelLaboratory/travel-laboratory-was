package site.travellaboratory.be.common.infrastructure.aws;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.error.ErrorCodes;

@Component
@Transactional
public class S3FileUploader {

    private final AmazonS3Client amazonS3Client;
    private final String bucket;

    public S3FileUploader(
        AmazonS3Client amazonS3Client,
        @Value("${cloud.aws.s3.bucket}") String bucket) {
        this.bucket = bucket;
        this.amazonS3Client = amazonS3Client;
    }

    public String uploadFiles(final MultipartFile file) {
        verifyFile(file);

        try {
            String fileName = file.getOriginalFilename();
            String fileUrl = "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + fileName;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
            return fileUrl;
        } catch (IOException e) {
            throw new BeApplicationException(ErrorCodes.FILE_UPLOAD_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
