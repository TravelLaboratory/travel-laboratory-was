package site.travellaboratory.be.presentation.articleschedule.dto.writer;

import java.util.List;

public record ArticleScheduleUpdateRequest(
    List<ArticleScheduleRequest> schedules
) {

}
