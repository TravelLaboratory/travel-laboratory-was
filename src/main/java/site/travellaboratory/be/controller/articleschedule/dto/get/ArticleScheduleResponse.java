package site.travellaboratory.be.controller.articleschedule.dto.get;

import java.sql.Time;
import java.time.LocalDate;

public record ArticleScheduleResponse(

    Long scheduleId,
    LocalDate visitedDate,
    Time visitedTime,
    Integer sortOrder,
    String category,
    Time durationTime,
    String expense,
    String memo,
    String dtype,
    ScheduleGeneralResponse scheduleGeneral,
    ScheduleTransportResponse scheduleTransport,
    ScheduleEtcResponse scheduleEtc
) {

}
