package site.travellaboratory.be.article.presentation.response._schedule.reader;

import java.util.List;

public record SchedulePlace(
    String visitedDate,
    List<PlaceName> placeNames
) {
}
