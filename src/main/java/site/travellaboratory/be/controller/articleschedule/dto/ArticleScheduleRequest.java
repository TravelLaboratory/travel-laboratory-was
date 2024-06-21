package site.travellaboratory.be.controller.articleschedule.dto;

import java.sql.Time;
import java.time.LocalDate;

public record ArticleScheduleRequest(
    LocalDate visitedDate,
    Time visitedTime,
    Integer sortOrder,
    String category,
    String dtype,
    ScheduleGeneralRequest scheduleGeneral,
    ScheduleTransportRequest scheduleTransport,
    ScheduleEtcRequest scheduleEtc
) {

}
