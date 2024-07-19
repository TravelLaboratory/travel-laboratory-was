package site.travellaboratory.be.article.domain._schedule.request;

import java.sql.Time;
import java.time.LocalDate;

public record ArticleScheduleRequest(
    Long scheduleId,
    LocalDate visitedDate,
    Time visitedTime,
    Integer sortOrder,
    String category,
    Time durationTime,
    String expense,
    String memo,

    String dtype,
    ScheduleGeneralRequest scheduleGeneral,
    ScheduleTransportRequest scheduleTransport,
    ScheduleEtcRequest scheduleEtc
) {

}
