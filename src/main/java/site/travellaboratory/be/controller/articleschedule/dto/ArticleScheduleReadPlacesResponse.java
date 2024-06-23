package site.travellaboratory.be.controller.articleschedule.dto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record ArticleScheduleReadPlacesResponse(
    Long articleId,
    int totalDays,
    List<SchedulePlace> schedules
) {

    public static ArticleScheduleReadPlacesResponse from(Long articleId, Map<String, List<String>> placesByDate) {
        List<SchedulePlace> schedulePlaces = placesByDate.entrySet().stream()
            .map(entry -> new SchedulePlace(
                entry.getKey(),  // 방문날짜
                entry.getValue().stream()
                    .map(PlaceName::new)
                    .collect(Collectors.toList())))
            .collect(Collectors.toList());

        return new ArticleScheduleReadPlacesResponse(
            articleId,
            schedulePlaces.size(),
            schedulePlaces
        );
    }
}
