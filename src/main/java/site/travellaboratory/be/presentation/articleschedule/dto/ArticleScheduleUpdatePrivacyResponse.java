package site.travellaboratory.be.presentation.articleschedule.dto;

public record ArticleScheduleUpdatePrivacyResponse(
    boolean isPrivate
) {
    public static ArticleScheduleUpdatePrivacyResponse from(boolean isPrivate) {
        return new ArticleScheduleUpdatePrivacyResponse(
            isPrivate
        );
    }
}
