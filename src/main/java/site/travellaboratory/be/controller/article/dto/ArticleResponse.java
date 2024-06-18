package site.travellaboratory.be.controller.article.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import site.travellaboratory.be.domain.article.Article;
import site.travellaboratory.be.domain.article.Location;
import site.travellaboratory.be.domain.article.TravelStyle;

public record ArticleResponse(
        String title,
        List<Location> location,
        LocalDate startAt,
        LocalDate endAt,
        String travelCompanion,
        List<String> travelStyle,
        String name
) {

    public static ArticleResponse from(final Article article) {
        List<String> travelStyleNames = article.getTravelStyles().stream()
                .map(TravelStyle::getName)
                .collect(Collectors.toList());

        return new ArticleResponse(
                article.getTitle(),
                article.getLocation(),
                article.getStartAt(),
                article.getEndAt(),
                article.getTravelCompanion().getName(),
                travelStyleNames,
                article.getUser().getNickname()
        );
    }

    public static List<ArticleResponse> from(final List<Article> articles) {
        return articles.stream()
                .map(ArticleResponse::from)
                .toList();
    }
}
