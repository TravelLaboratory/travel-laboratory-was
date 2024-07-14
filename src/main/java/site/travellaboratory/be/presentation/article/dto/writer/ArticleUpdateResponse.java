package site.travellaboratory.be.presentation.article.dto.writer;

import java.time.LocalDate;
import java.util.List;
import site.travellaboratory.be.domain.article.Article;
import site.travellaboratory.be.domain.article.enums.TravelStyle;

public record ArticleUpdateResponse(
    String title,
    List<LocationDto> location,
    LocalDate startAt,
    LocalDate endAt,
    String expense,
    String travelCompanion,
    List<String> travelStyles
) {
    public static ArticleUpdateResponse from(Article article) {
        List<String> travelStyleNames = article.getTravelStyles()
            .stream()
            .map(TravelStyle::getName)
            .toList();

        return new ArticleUpdateResponse(
            article.getTitle(),
            article.getLocations().stream()
                .map(LocationDto::from)
                .toList(),
            article.getStartAt(),
            article.getEndAt(),
            article.getExpense(),
            article.getTravelCompanion().getName(),
            travelStyleNames
        );
    }
}
