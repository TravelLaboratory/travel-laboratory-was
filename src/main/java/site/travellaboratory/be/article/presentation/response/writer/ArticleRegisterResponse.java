package site.travellaboratory.be.article.presentation.response.writer;

public record ArticleRegisterResponse(
        Long articleId
) {
    public static ArticleRegisterResponse from(final Long id) {
        return new ArticleRegisterResponse(id);
    }
}
