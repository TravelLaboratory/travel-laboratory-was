package site.travellaboratory.be.article.domain._schedule.request;

import java.util.List;

public record ArticleScheduleUpdateRequest(
    List<ArticleScheduleRequest> schedules
) {

}
