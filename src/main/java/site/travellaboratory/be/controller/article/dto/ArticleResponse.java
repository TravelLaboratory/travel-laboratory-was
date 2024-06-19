package site.travellaboratory.be.controller.article.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
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
        String name,
        int bookmarkCount,
        boolean isBookmarked
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
                article.getUser().getNickname(),
                article.getBookmarkCount(),
                article.isBookmarked()
        );
    }

    public static Page<ArticleResponse> from(final Page<Article> articles) {
        return articles.map(ArticleResponse::from);
    }
}
