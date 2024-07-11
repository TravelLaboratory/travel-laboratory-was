package site.travellaboratory.be.presentation.article.dto;

import java.time.LocalDate;
import java.util.List;
import site.travellaboratory.be.infrastructure.domains.article.entity.Location;

public record ArticleRegisterRequest(
        String title,
        List<Location> location,
        LocalDate startAt,
        LocalDate endAt,
        String expense,
        String travelCompanion,
        List<String> travelStyles
) {
}
