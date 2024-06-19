package site.travellaboratory.be.controller.article.dto;

public record ArticleRegisterResponse(
        Long articleId
) {
    public static ArticleRegisterResponse from(final Long id) {
        return new ArticleRegisterResponse(id);
    }
}
