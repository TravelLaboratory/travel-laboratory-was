package site.travellaboratory.be.controller.articleschedule.dto;

public record ArticleScheduleUpdateResponse(
    Long articleId
) {

    public static ArticleScheduleUpdateResponse from(Long articleId) {
        return new ArticleScheduleUpdateResponse(
            articleId
        );
    }
}


