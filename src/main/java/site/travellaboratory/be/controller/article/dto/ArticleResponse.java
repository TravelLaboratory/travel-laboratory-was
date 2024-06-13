package site.travellaboratory.be.controller.article.dto;

import java.time.LocalDateTime;
import java.util.List;
import site.travellaboratory.be.domain.article.Article;

public record ArticleResponse(
        Long id,
        String tripSpotName,
        String name,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String expense,
        String imageUrl
) {

    public static ArticleResponse from(final Article article) {
        return new ArticleResponse(article.getId(), article.getTitle(), article.getNickname(), article.getStartAt(),
                article.getEndAt(),
                article.getExpense(), article.getImageUrl());
    }

    public static List<ArticleResponse> from(final List<Article> articles) {
        return articles.stream()
                .map(ArticleResponse::from)
                .toList();
    }
}
