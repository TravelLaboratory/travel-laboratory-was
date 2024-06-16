package site.travellaboratory.be.controller.article.dto;

import site.travellaboratory.be.domain.article.Article;
import site.travellaboratory.be.domain.article.ArticleStatus;

public record ArticleAuthorityResponse(
        ArticleStatus status
) {

    public static ArticleAuthorityResponse from(final Article article) {
        return new ArticleAuthorityResponse(article.getStatus());
    }
}
