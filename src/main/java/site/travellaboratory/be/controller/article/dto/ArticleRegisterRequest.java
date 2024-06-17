package site.travellaboratory.be.controller.article.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ArticleRegisterRequest(
        String title,
        List<String> location,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String expense,
        String travelCompanion,
        List<String> style
) {
}
