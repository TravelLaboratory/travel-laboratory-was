package site.travellaboratory.be.controller.article.dto;

import java.time.LocalDateTime;
import java.util.List;
import site.travellaboratory.be.domain.article.Article;

public record ArticleSearchResponse(
        String title,
        List<String> location,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String expense,
        List<String> travelCompanion,
        List<String> style,
        String nickname
) {

    public static List<ArticleSearchResponse> from(final List<Article> articles) {
        return articles.stream()
                .map(article -> new ArticleSearchResponse(
                        article.getTitle(),
                        article.getLocation(),
                        article.getStartAt(),
                        article.getEndAt(),
                        article.getExpense(),
                        article.getTravelCompanions(),
                        article.getTravelStyles(),
                        article.getNickname()
                ))
                .toList();
    }
}
