package site.travellaboratory.be.common.infrastructure.aws;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;

@ExtendWith(MockitoExtension.class)
class S3FileUploaderTest {

    @InjectMocks
    private S3FileUploader sut;

    @Mock
    private AmazonS3Client amazonS3Client;

    @Mock
    private MultipartFile multipartFile;

    private final String bucket = "test_bucket";

    @BeforeEach
    void setUp() {
        this.sut = new S3FileUploader(amazonS3Client, bucket);
    }

    @Nested
    class uploadFiles {
        @DisplayName("빈_파일_업로드_시_예외_반환")
        @Test
        void test1() {
            //given
            when(multipartFile.isEmpty()).thenReturn(true);

            //when
            BeApplicationException exception = assertThrows(BeApplicationException.class, () ->
                sut.uploadFiles(multipartFile)
            );

            //then
            assertEquals(ErrorCodes.FILE_IS_EMPTY, exception.getErrorCodes());
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        }

        @DisplayName("파일_이름이_없는_경우_예외_반환")
        @Test
        void test2() {
            //given
            when(multipartFile.getOriginalFilename()).thenReturn("");

            //when
            BeApplicationException exception = assertThrows(BeApplicationException.class, () ->
                sut.uploadFiles(multipartFile)
            );

            //then
            assertEquals(ErrorCodes.FILE_NAME_EMPTY, exception.getErrorCodes());
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        }

        @DisplayName("파일_크기가_0인_경우_예외_반환")
        @Test
        void test3() {
            //given
            when(multipartFile.getOriginalFilename()).thenReturn("imageFile");
            when(multipartFile.getSize()).thenReturn(0L);

            //when
            BeApplicationException exception = assertThrows(BeApplicationException.class, () ->
                sut.uploadFiles(multipartFile));

            //then
            assertEquals(ErrorCodes.FILE_SIZE_ZERO, exception.getErrorCodes());
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        }

        @DisplayName("파일_업로드_중_IOException_으로_인해_실패할_경우_예외_반환")
        @Test
        void test4() throws IOException {
            //given
            String fileName = "test.jpg";
            String content = "fake image file";

            when(multipartFile.getOriginalFilename()).thenReturn(fileName);
            when(multipartFile.getSize()).thenReturn((long) content.length());
            when(multipartFile.getInputStream()).thenThrow(new IOException("test IOException"));

            //when
            BeApplicationException exception = assertThrows(BeApplicationException.class, () ->
                sut.uploadFiles(multipartFile)
            );

            //then
            assertEquals(ErrorCodes.FILE_UPLOAD_FAILED, exception.getErrorCodes());
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
        }

        @DisplayName("성공 - S3에_파일_업로드")
        @Test
        void test1000() throws IOException {
            //given
            String fileName = "test.jpg";
            String fileContent = "fake image content";
            String fileUrl = "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + fileName;
            InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());

            when(multipartFile.getOriginalFilename()).thenReturn(fileName);
            when(multipartFile.getContentType()).thenReturn("image/jpeg");
            when(multipartFile.getSize()).thenReturn((long) fileContent.length());
            when(multipartFile.getInputStream()).thenReturn(inputStream);

            //when
            String result = sut.uploadFiles(multipartFile);

            //then
            assertEquals(fileUrl, result);
            verify(amazonS3Client).putObject(eq(bucket), eq(fileName), eq(inputStream), any(ObjectMetadata.class));
        }
    }
}