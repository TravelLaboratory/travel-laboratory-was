package site.travellaboratory.be.article.domain._schedule;

import java.sql.Time;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import site.travellaboratory.be.article.domain.Article;
import site.travellaboratory.be.article.domain._schedule.enums.ArticleScheduleStatus;
import site.travellaboratory.be.article.presentation.response._schedule.writer.ArticleScheduleRequest;

@Getter
public class ScheduleTransport extends ArticleSchedule {

    private final String transportation;
    private final String startPlaceName;
    private final String googleMapStartPlaceAddress;
    private final Double googleMapStartLatitude;
    private final Double googleMapStartLongitude;
    private final String endPlaceName;
    private final String googleMapEndPlaceAddress;
    private final Double googleMapEndLatitude;
    private final Double googleMapEndLongitude;

    @Builder
    public ScheduleTransport(
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
        String transportation,
        String startPlaceName,
        String googleMapStartPlaceAddress,
        Double googleMapStartLatitude,
        Double googleMapStartLongitude,
        String endPlaceName,
        String googleMapEndPlaceAddress,
        Double googleMapEndLatitude,
        Double googleMapEndLongitude) {
        super(id, article, visitedDate, visitedTime, sortOrder, category, durationTime, expense, memo, status, dtype);
        this.transportation = transportation;
        this.startPlaceName = startPlaceName;
        this.googleMapStartPlaceAddress = googleMapStartPlaceAddress;
        this.googleMapStartLatitude = googleMapStartLatitude;
        this.googleMapStartLongitude = googleMapStartLongitude;
        this.endPlaceName = endPlaceName;
        this.googleMapEndPlaceAddress = googleMapEndPlaceAddress;
        this.googleMapEndLatitude = googleMapEndLatitude;
        this.googleMapEndLongitude = googleMapEndLongitude;
    }

    public static ScheduleTransport create(Article article, ArticleScheduleRequest request) {
        return ScheduleTransport.builder()
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
            .transportation(request.scheduleTransport().transportation())
            .startPlaceName(request.scheduleTransport().startPlaceName())
            .googleMapStartPlaceAddress(request.scheduleTransport().googleMapStartPlaceAddress())
            .googleMapStartLatitude(request.scheduleTransport().googleMapStartLatitude())
            .googleMapStartLongitude(request.scheduleTransport().googleMapStartLongitude())
            .endPlaceName(request.scheduleTransport().endPlaceName())
            .googleMapEndPlaceAddress(request.scheduleTransport().googleMapEndPlaceAddress())
            .googleMapEndLatitude(request.scheduleTransport().googleMapEndLatitude())
            .googleMapEndLongitude(request.scheduleTransport().googleMapEndLongitude())
            .build();
    }

    @Override
    public ScheduleTransport update(ArticleScheduleRequest request) {
        super.verifyArticleSchedule(request.scheduleId());
        return ScheduleTransport.builder()
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
            .dtype(request.dtype())
            .transportation(request.scheduleTransport().transportation())
            .startPlaceName(request.scheduleTransport().startPlaceName())
            .googleMapStartPlaceAddress(request.scheduleTransport().googleMapStartPlaceAddress())
            .googleMapStartLatitude(request.scheduleTransport().googleMapStartLatitude())
            .googleMapStartLongitude(request.scheduleTransport().googleMapStartLongitude())
            .endPlaceName(request.scheduleTransport().endPlaceName())
            .googleMapEndPlaceAddress(request.scheduleTransport().googleMapEndPlaceAddress())
            .googleMapEndLatitude(request.scheduleTransport().googleMapEndLatitude())
            .googleMapEndLongitude(request.scheduleTransport().googleMapEndLongitude())
            .build();
    }

    @Override
    public ScheduleTransport delete() {
        return ScheduleTransport.builder()
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
            .transportation(this.transportation)
            .startPlaceName(this.startPlaceName)
            .googleMapStartPlaceAddress(this.googleMapStartPlaceAddress)
            .googleMapStartLatitude(this.googleMapStartLatitude)
            .googleMapStartLongitude(this.googleMapStartLongitude)
            .endPlaceName(this.endPlaceName)
            .googleMapEndPlaceAddress(this.googleMapEndPlaceAddress)
            .googleMapEndLatitude(this.googleMapEndLatitude)
            .googleMapEndLongitude(this.googleMapEndLongitude)
            .build();
    }

    //    public void update(ScheduleTransportRequest request) {
//        this.transportation = request.transportation();
//        this.startPlaceName = request.startPlaceName();
//        this.googleMapStartPlaceAddress = request.googleMapStartPlaceAddress();
//        this.googleMapStartLatitude = request.googleMapStartLatitude();
//        this.googleMapStartLongitude = request.googleMapStartLongitude();
//        this.endPlaceName = request.endPlaceName();
//        this.googleMapEndPlaceAddress = request.googleMapEndPlaceAddress();
//        this.googleMapEndLatitude = request.googleMapEndLatitude();
//        this.googleMapEndLongitude = request.googleMapEndLongitude();
//    }
}
