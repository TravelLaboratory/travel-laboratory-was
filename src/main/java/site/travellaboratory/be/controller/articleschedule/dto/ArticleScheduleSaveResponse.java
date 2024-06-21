package site.travellaboratory.be.controller.articleschedule.dto;

public record ArticleScheduleSaveResponse(
    Long articleId
) {

    public static ArticleScheduleSaveResponse from(Long articleId) {
        return new ArticleScheduleSaveResponse(
            articleId
        );
    }
}


