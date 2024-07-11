package site.travellaboratory.be.presentation.articleschedule.dto.writer;

public record ArticleScheduleDeleteResponse(
    Boolean isDelete
) {
    public static ArticleScheduleDeleteResponse from(Boolean isDelete) {
        return new ArticleScheduleDeleteResponse(
            isDelete
        );
    }
}
