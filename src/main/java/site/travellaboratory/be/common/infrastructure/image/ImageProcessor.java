package site.travellaboratory.be.common.infrastructure.image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageProcessor {

    public ByteArrayOutputStream resizeAndCompressImage(MultipartFile file,
        int width, int height, double quality) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(file.getInputStream())
            .size(width, height)
            .crop(Positions.CENTER)
            .outputQuality(quality)
            .toOutputStream(outputStream);
        return outputStream;
    }
}
