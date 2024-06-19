package site.travellaboratory.be.controller.article.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import site.travellaboratory.be.domain.article.Article;
import site.travellaboratory.be.domain.article.Location;
import site.travellaboratory.be.domain.article.TravelStyle;

public record ArticleUpdateResponse(
        String title,
        List<Location> location,
        LocalDate startAt,
        LocalDate endAt,
        String expense,
        String travelCompanion,
        List<String> travelStyles,
        int bookmarkCount,
        boolean isBookmarked
) {
    public static ArticleUpdateResponse from(final Article article) {
        List<String> travelStyleNames = article.getTravelStyles()
                .stream()
                .map(TravelStyle::getName)
                .collect(Collectors.toList());

        return new ArticleUpdateResponse(
                article.getTitle(),
                article.getLocation(),
                article.getStartAt(),
                article.getEndAt(),
                article.getExpense(),
                article.getTravelCompanion().getName(),
                travelStyleNames,
                article.getBookmarkCount(),
                article.isBookmarked()
        );
    }
}
