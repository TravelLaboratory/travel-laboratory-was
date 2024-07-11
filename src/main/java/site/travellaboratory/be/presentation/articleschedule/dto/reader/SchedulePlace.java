package site.travellaboratory.be.presentation.articleschedule.dto.reader;

import java.util.List;

public record SchedulePlace(
    String visitedDate,
    List<PlaceName> placeNames
) {
}
