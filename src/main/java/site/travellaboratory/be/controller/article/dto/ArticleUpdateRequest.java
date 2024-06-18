package site.travellaboratory.be.controller.article.dto;

import java.time.LocalDate;
import java.util.List;
import site.travellaboratory.be.domain.article.Location;

public record ArticleUpdateRequest(
        String title,
        List<Location> location,
        LocalDate startAt,
        LocalDate endAt,
        String expense,
        String travelCompanion,
        List<String> style
) {
}
