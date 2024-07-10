package site.travellaboratory.be.presentation.articleschedule.dto.put;

import java.util.List;

public record ArticleScheduleUpdateRequest(
    List<ArticleScheduleRequest> schedules
) {

}
