package site.travellaboratory.be.presentation.articleschedule.dto.put;

public record ArticleScheduleUpdateResponse(
    Long articleId
) {

    public static ArticleScheduleUpdateResponse from(Long articleId) {
        return new ArticleScheduleUpdateResponse(
            articleId
        );
    }
}


