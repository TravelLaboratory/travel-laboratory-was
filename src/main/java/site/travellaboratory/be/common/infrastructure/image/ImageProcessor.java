package site.travellaboratory.be.common.infrastructure.image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageProcessor {

    public ByteArrayOutputStream resizeAndCompressProfileImage(MultipartFile file) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(file.getInputStream())
            .size(250, 250)
            .outputQuality(0.8)
            .toOutputStream(outputStream);
        return outputStream;
    }

    public ByteArrayOutputStream resizeAndCompressBackgroundImage(MultipartFile file) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(file.getInputStream())
            .size(1920, 560)
            .crop(Positions.CENTER)
            .outputQuality(0.8)
            .toOutputStream(outputStream);
        return outputStream;
    }
}
