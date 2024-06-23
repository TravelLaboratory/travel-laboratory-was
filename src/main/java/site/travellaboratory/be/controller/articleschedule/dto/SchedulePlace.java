package site.travellaboratory.be.controller.articleschedule.dto;

import java.util.List;

public record SchedulePlace(
    String visitedDate,
    List<PlaceName> placeNames
) {
}
