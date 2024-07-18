package site.travellaboratory.be.article.domain.request;

import java.time.LocalDate;
import java.util.List;


public record ArticleUpdateRequest(
        String title,
        List<LocationRequest> locations,
        LocalDate startAt,
        LocalDate endAt,
        String expense,
        String travelCompanion,
        List<String> travelStyles
) {
}
