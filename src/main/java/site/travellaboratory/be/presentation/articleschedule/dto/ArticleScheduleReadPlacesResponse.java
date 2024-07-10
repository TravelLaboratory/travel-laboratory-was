package site.travellaboratory.be.presentation.articleschedule.dto;

import java.util.List;

public record ArticleScheduleReadPlacesResponse(
    Long articleId,
    int totalDays,
    List<SchedulePlace> schedules
) {
    public static ArticleScheduleReadPlacesResponse from(Long articleId, List<SchedulePlace> schedulePlaces) {

        return new ArticleScheduleReadPlacesResponse(
            articleId,
            schedulePlaces.size(),
            schedulePlaces
        );
    }
}
