package site.travellaboratory.be.domain.article;

import java.sql.Time;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import site.travellaboratory.be.domain.article.enums.ArticleScheduleStatus;

@Getter
public class ScheduleEtc extends ArticleSchedule {

    private final String placeName;

    @Builder
    public ScheduleEtc(
        Long id,
        Article article,
        LocalDate visitedDate,
        Time visitedTime,
        Integer sortOrder,
        String category,
        Time durationTime,
        String expense,
        String memo,
        ArticleScheduleStatus status,
        String dtype,
        String placeName) {
        super(id, article, visitedDate, visitedTime, sortOrder, category, durationTime, expense, memo, status, dtype);
        this.placeName = placeName;
    }

    //    public void update(ScheduleEtcRequest request) {
//        this.placeName = request.placeName();
//    }
}
