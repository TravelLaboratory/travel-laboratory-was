package site.travellaboratory.be.controller.article.dto;

import java.time.LocalDateTime;

public record ArticleRegisterRequest(
        String title,
        String[] location,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String expense,
        String[] travelCompanion,
        String[] style
) {
}
