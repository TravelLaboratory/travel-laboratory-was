package site.travellaboratory.be.presentation.article.dto.writer;

public record ArticleDeleteResponse(
    Boolean isDelete
) {
    public static ArticleDeleteResponse from(Boolean isDelete) {
        return new ArticleDeleteResponse(isDelete);
    }
}
