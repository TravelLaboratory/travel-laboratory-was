package site.travellaboratory.be.presentation.article.dto;

public record ArticleDeleteResponse(
    Boolean isDelete
) {
    public static ArticleDeleteResponse from(Boolean isDelete) {
        return new ArticleDeleteResponse(isDelete);
    }
}
