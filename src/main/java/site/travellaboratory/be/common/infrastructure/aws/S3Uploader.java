package site.travellaboratory.be.common.infrastructure.aws;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;
    private final String bucket;

    public S3Uploader(
        AmazonS3Client amazonS3Client,
        @Value("${cloud.aws.s3.bucket}") String bucket) {
        this.bucket = bucket;
        this.amazonS3Client = amazonS3Client;
    }

    public String uploadImageFile(String fileName, ByteArrayOutputStream outputStream, String folder) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(outputStream.size());

        String contentType = determineContentType(fileName);
        metadata.setContentType(contentType);

        amazonS3Client.putObject(bucket, folder + "/" + fileName,
            new ByteArrayInputStream(outputStream.toByteArray()), metadata);
        return "https://" + bucket + ".s3.amazonaws.com/" + folder + "/" + fileName;
    }

    private String determineContentType(String fileName) {
        if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".gif")) {
            return "image/gif";
        } else {
            return "image/jpeg";
        }
    }
}
