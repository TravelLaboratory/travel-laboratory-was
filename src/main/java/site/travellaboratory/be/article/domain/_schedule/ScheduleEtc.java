package site.travellaboratory.be.article.domain._schedule;

import java.sql.Time;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import site.travellaboratory.be.article.domain.Article;
import site.travellaboratory.be.article.domain._schedule.enums.ArticleScheduleStatus;
import site.travellaboratory.be.article.presentation.response._schedule.writer.ArticleScheduleRequest;

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
        super(id, article, visitedDate, visitedTime, sortOrder, category, durationTime, expense,
            memo, status, dtype);
        this.placeName = placeName;
    }

    public static ScheduleEtc create(
        Article article, ArticleScheduleRequest request) {
        return ScheduleEtc.builder()
            .article(article)
            .visitedDate(request.visitedDate())
            .visitedTime(request.visitedTime())
            .sortOrder(request.sortOrder())
            .category(request.category())
            .durationTime(request.durationTime())
            .expense(request.expense())
            .memo(request.memo())
            .status(ArticleScheduleStatus.ACTIVE)
            .dtype(request.dtype())
            .placeName(request.scheduleEtc().placeName())
            .build();
    }

    @Override
    public ScheduleEtc update(ArticleScheduleRequest request) {
        super.verifyArticleSchedule(request.scheduleId());
        return ScheduleEtc.builder()
            .id(request.scheduleId())
            .article(this.getArticle())
            .visitedDate(request.visitedDate())
            .visitedTime(request.visitedTime())
            .sortOrder(request.sortOrder())
            .category(request.category())
            .durationTime(request.durationTime())
            .expense(request.expense())
            .memo(request.memo())
            .status(this.getStatus())
            .dtype(request.dtype())
            .placeName(request.scheduleEtc().placeName())
            .build();
    }

    @Override
    public ScheduleEtc delete() {
        return ScheduleEtc.builder()
            .id(this.getId())
            .article(this.getArticle())
            .visitedDate(this.getVisitedDate())
            .visitedTime(this.getVisitedTime())
            .sortOrder(this.getSortOrder())
            .category(this.getCategory())
            .durationTime(this.getDurationTime())
            .expense(this.getExpense())
            .memo(this.getMemo())
            .status(ArticleScheduleStatus.INACTIVE)
            .dtype(this.getDtype())
            .placeName(this.getPlaceName())
            .build();
    }

    //    public void update(ScheduleEtcRequest request) {
//        this.placeName = request.placeName();
//    }
}
