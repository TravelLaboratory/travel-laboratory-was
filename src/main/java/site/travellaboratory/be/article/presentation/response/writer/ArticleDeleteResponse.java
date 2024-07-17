package site.travellaboratory.be.article.presentation.response.writer;

public record ArticleDeleteResponse(
    Boolean isDelete
) {
    public static ArticleDeleteResponse from(Boolean isDelete) {
        return new ArticleDeleteResponse(isDelete);
    }
}
