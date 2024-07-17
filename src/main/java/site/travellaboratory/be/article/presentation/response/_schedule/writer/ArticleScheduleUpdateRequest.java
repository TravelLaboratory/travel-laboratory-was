package site.travellaboratory.be.article.presentation.response._schedule.writer;

import java.util.List;

public record ArticleScheduleUpdateRequest(
    List<ArticleScheduleRequest> schedules
) {

}
