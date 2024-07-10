package site.travellaboratory.be.presentation.articleschedule.dto.put;

import java.sql.Time;
import java.time.LocalDate;

public record ArticleScheduleRequest(
    // 수정 시 사용 todo: 리팩토링 시 id 때문에 분리 예정
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
