package site.travellaboratory.be.common.infrastructure.aws;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class S3UploaderTest {

    @InjectMocks
    private S3Uploader sut;

    @Mock
    private AmazonS3Client amazonS3Client;

    private final String bucket = "test_bucket";

    @BeforeEach
    void setUp() {
        this.sut = new S3Uploader(amazonS3Client, bucket);
    }

    @Nested
    @DisplayName("S3Uploader 클래스의 uploadImageFile 메서드 테스트")
    class UploadImageFileTests {

        @Test
        @DisplayName("성공적으로 파일을 업로드하고 URL을 반환한다")
        void uploadImageFile_Success() throws Exception {
            //given
            String fileName = "test.jpg";
            String folder = "folder";
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write("fake image content".getBytes());

            //mock S3Client behavior
            when(amazonS3Client.putObject(eq(bucket), eq(folder + "/" + fileName), any(ByteArrayInputStream.class), any(ObjectMetadata.class)))
                .thenReturn(null);

            //when
            String result = sut.uploadImageFile(fileName, outputStream, folder);

            //then
            String expectedUrl = "https://" + bucket + ".s3.amazonaws.com/" + folder + "/" + fileName;
            assertEquals(expectedUrl, result);
            verify(amazonS3Client).putObject(eq(bucket), eq(folder + "/" + fileName), any(ByteArrayInputStream.class), any(ObjectMetadata.class));
        }
    }
}
