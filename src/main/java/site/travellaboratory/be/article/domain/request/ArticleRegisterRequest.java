package site.travellaboratory.be.article.domain.request;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

public record ArticleRegisterRequest(
    String title,
    List<LocationRequest> locations,
    LocalDate startAt,
    LocalDate endAt,
    String expense,
    String travelCompanion,
    List<String> travelStyles
) {
    @Builder
    public ArticleRegisterRequest {
    }
}
