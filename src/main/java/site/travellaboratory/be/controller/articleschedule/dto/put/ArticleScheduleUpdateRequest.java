package site.travellaboratory.be.controller.articleschedule.dto.put;

import java.util.List;

public record ArticleScheduleUpdateRequest(
    List<ArticleScheduleRequest> schedules
) {

}
