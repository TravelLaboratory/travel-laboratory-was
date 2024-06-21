package site.travellaboratory.be.controller.articleschedule.dto;

import java.util.List;

public record ArticleScheduleSaveRequest(
    List<ArticleScheduleRequest> schedules
) {

}
