package site.travellaboratory.be.article.presentation.response._schedule.writer;

public record ArticleScheduleUpdateResponse(
    Long articleId
) {

    public static ArticleScheduleUpdateResponse from(Long articleId) {
        return new ArticleScheduleUpdateResponse(
            articleId
        );
    }
}


