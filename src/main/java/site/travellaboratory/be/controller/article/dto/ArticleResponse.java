package site.travellaboratory.be.controller.article.dto;

import java.time.LocalDateTime;
import java.util.List;
import site.travellaboratory.be.domain.article.Article;

public record ArticleResponse(
        String title,
        List<String> location,
        LocalDateTime startAt,
        LocalDateTime endAt,
        List<String> travelCompanions,
        List<String> style,
        String imageUrl,
        String name
) {

    public static ArticleResponse from(final Article article) {
        return new ArticleResponse(article.getTitle(), article.getLocation(),article.getStartAt(),article.getEndAt(),
                article.getTravelCompanions(),article.getTravelStyles(),article.getImageUrl(),
                article.getUser().getNickname());
    }

    public static List<ArticleResponse> from(final List<Article> articles) {
        return articles.stream()
                .map(ArticleResponse::from)
                .toList();
    }
}
