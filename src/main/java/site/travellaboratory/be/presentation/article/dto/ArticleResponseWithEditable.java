package site.travellaboratory.be.presentation.article.dto;

public record ArticleResponseWithEditable(
        ArticleTotalResponse articleTotalResponse,
        boolean isEditable
) {
}
