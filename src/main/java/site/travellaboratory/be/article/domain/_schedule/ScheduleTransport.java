package site.travellaboratory.be.article.domain._schedule;

import java.sql.Time;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import site.travellaboratory.be.article.domain.Article;
import site.travellaboratory.be.article.domain._schedule.enums.ArticleScheduleStatus;
import site.travellaboratory.be.article.domain._schedule.request.ArticleScheduleRequest;
import site.travellaboratory.be.user.domain.User;

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

    public static ScheduleTransport create(User user, Article article, ArticleScheduleRequest request) {
        // 유저의 여행계획이 맞는지
        article.verifyOwner(user);
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
    public ScheduleTransport update(User user, ArticleScheduleRequest request) {
        // 유저의 여행계획이 맞는지
        this.getArticle().verifyOwner(user);
        // 유효하지 않은 일정을 수정하려는 경우 (삭제 혹은 임의의 scheduleId를 넣어서 요청한 경우)
        super.verifyArticleScheduleId(request.scheduleId());
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
    public ScheduleTransport delete(User user) {
        // 유저의 여행계획이 맞는지
        this.getArticle().verifyOwner(user);
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
}
