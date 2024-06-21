package site.travellaboratory.be.controller.articleschedule.dto;

public record ArticleScheduleDeleteResponse(
    Boolean isDelete
) {
    public static ArticleScheduleDeleteResponse from(Boolean isDelete) {
        return new ArticleScheduleDeleteResponse(
            isDelete
        );
    }
}
