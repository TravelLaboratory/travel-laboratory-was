package site.travellaboratory.be.article.domain._schedule;

import java.sql.Time;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import site.travellaboratory.be.article.domain.Article;
import site.travellaboratory.be.article.domain._schedule.enums.ArticleScheduleStatus;
import site.travellaboratory.be.article.presentation.response._schedule.writer.ArticleScheduleRequest;

@Getter
public class ScheduleGeneral extends ArticleSchedule {

    private final String placeName;
    private final String googleMapPlaceId;
    private final Double googleMapLatitude;
    private final Double googleMapLongitude;
    private final String googleMapAddress;
    private final String googleMapPhoneNumber;
    private final String googleMapHomePageUrl;

    @Builder
    public ScheduleGeneral(
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
        String placeName,
        String googleMapPlaceId,
        Double googleMapLatitude,
        Double googleMapLongitude,
        String googleMapAddress,
        String googleMapPhoneNumber,
        String googleMapHomePageUrl) {
        super(id, article, visitedDate, visitedTime, sortOrder, category, durationTime, expense, memo, status, dtype);
        this.placeName = placeName;
        this.googleMapPlaceId = googleMapPlaceId;
        this.googleMapLatitude = googleMapLatitude;
        this.googleMapLongitude = googleMapLongitude;
        this.googleMapAddress = googleMapAddress;
        this.googleMapPhoneNumber = googleMapPhoneNumber;
        this.googleMapHomePageUrl = googleMapHomePageUrl;
    }

    public static ScheduleGeneral create(
        Article article, ArticleScheduleRequest request) {
        return ScheduleGeneral.builder()
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
            .placeName(request.scheduleGeneral().placeName())
            .googleMapPlaceId(request.scheduleGeneral().googleMapPlaceId())
            .googleMapLatitude(request.scheduleGeneral().googleMapLatitude())
            .googleMapLongitude(request.scheduleGeneral().googleMapLongitude())
            .googleMapAddress(request.scheduleGeneral().googleMapAddress())
            .googleMapPhoneNumber(request.scheduleGeneral().googleMapPhoneNumber())
            .googleMapHomePageUrl(request.scheduleGeneral().googleMapHomePageUrl())
            .build();
    }

    @Override
    public ScheduleGeneral update(ArticleScheduleRequest request) {
        super.verifyArticleSchedule(request.scheduleId());
        return ScheduleGeneral.builder()
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
            .googleMapPlaceId(request.scheduleGeneral().googleMapPlaceId())
            .googleMapLatitude(request.scheduleGeneral().googleMapLatitude())
            .googleMapLongitude(request.scheduleGeneral().googleMapLongitude())
            .googleMapAddress(request.scheduleGeneral().googleMapAddress())
            .googleMapPhoneNumber(request.scheduleGeneral().googleMapPhoneNumber())
            .googleMapHomePageUrl(request.scheduleGeneral().googleMapHomePageUrl())
            .build();
    }

    @Override
    public ScheduleGeneral delete() {
        return ScheduleGeneral.builder()
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
            .placeName(this.placeName)
            .googleMapPlaceId(this.googleMapPlaceId)
            .googleMapLatitude(this.googleMapLatitude)
            .googleMapLongitude(this.googleMapLongitude)
            .googleMapAddress(this.googleMapAddress)
            .googleMapPhoneNumber(this.googleMapPhoneNumber)
            .googleMapHomePageUrl(this.googleMapHomePageUrl)
            .build();
    }

    //    public void update(ScheduleGeneralRequest request) {
//        this.placeName = request.placeName();
//        this.googleMapPlaceId = request.googleMapPlaceId();
//        this.googleMapLatitude = request.googleMapLatitude();
//        this.googleMapLongitude = request.googleMapLongitude();
//        this.googleMapAddress = request.googleMapAddress();
//        this.googleMapPhoneNumber = request.googleMapPhoneNumber();
//        this.googleMapHomePageUrl = request.googleMapHomePageUrl();
//    }
}
