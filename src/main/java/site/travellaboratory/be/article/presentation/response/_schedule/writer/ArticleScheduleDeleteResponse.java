package site.travellaboratory.be.article.presentation.response._schedule.writer;

public record ArticleScheduleDeleteResponse(
    Boolean isDelete
) {
    public static ArticleScheduleDeleteResponse from(Boolean isDelete) {
        return new ArticleScheduleDeleteResponse(
            isDelete
        );
    }
}
