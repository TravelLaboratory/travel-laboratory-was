package site.travellaboratory.be.presentation.articleschedule.dto.reader;

import java.sql.Time;
import java.time.LocalDate;

// todo : jpa entity 이름 수정 후엔 response 제거
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
