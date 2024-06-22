package site.travellaboratory.be.controller.article.dto;

public record ArticleResponseWithEditable(
        ArticleTotalResponse articleTotalResponse,
        boolean isEditable
) {
}
