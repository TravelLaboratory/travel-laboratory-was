package site.travellaboratory.be.infrastructure.aws;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
@Transactional
public class S3FileUploader {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;

    public String uploadFiles(final MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            String fileUrl = "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + fileName;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
            return fileUrl;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("File upload failed", e);
        }
    }
}
