package site.travellaboratory.be.presentation.article.dto.writer;

public record ArticleRegisterResponse(
        Long articleId
) {
    public static ArticleRegisterResponse from(final Long id) {
        return new ArticleRegisterResponse(id);
    }
}
