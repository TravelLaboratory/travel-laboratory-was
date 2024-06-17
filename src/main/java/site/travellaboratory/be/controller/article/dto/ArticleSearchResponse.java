package site.travellaboratory.be.controller.article.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import site.travellaboratory.be.domain.article.Article;
import site.travellaboratory.be.domain.article.TravelStyle;

public record ArticleSearchResponse(
        String title,
        List<String> location,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String expense,
        String travelCompanion,
        List<String> travelStyle,
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
                        article.getTravelCompanion().name(),
                        article.getTravelStyles().stream()
                                .map(TravelStyle::getName)
                                .collect(Collectors.toList()),
                        article.getNickname()
                ))
                .toList();
    }
}
