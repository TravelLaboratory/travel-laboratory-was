package site.travellaboratory.be.controller.dto.article;

import java.time.LocalDateTime;

public record ArticleRegisterRequest(
        LocalDateTime startAt,
        LocalDateTime endAt,
        String title,
        String imageUrl,
        String expense
) {
}
