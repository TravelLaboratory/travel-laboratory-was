package site.travellaboratory.be.article.infrastructure.persistence.entity._schedule;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.travellaboratory.be.article.domain._schedule.ScheduleGeneral;
import site.travellaboratory.be.article.infrastructure.persistence.entity.ArticleEntity;
import site.travellaboratory.be.article.domain._schedule.request.ScheduleGeneralRequest;

@Entity
@Table(name = "schedule_general")
@DiscriminatorValue("GENERAL")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleGeneralEntity extends ArticleScheduleEntity {

    @Column(nullable = false, length = 255)
    private String placeName;

    @Column(nullable = false, length = 255)
    private String googleMapPlaceId;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,8)")
    private Double googleMapLatitude;

    @Column(nullable = false, columnDefinition = "DECIMAL(11,8)")
    private Double googleMapLongitude;

    @Column(length = 500)
    private String googleMapAddress;

    @Column(length = 20)
    private String googleMapPhoneNumber;

    @Column(length = 1000)
    private String googleMapHomePageUrl;

    public static ScheduleGeneralEntity from(ScheduleGeneral scheduleGeneral) {
        ScheduleGeneralEntity result = new ScheduleGeneralEntity();
        result.id = scheduleGeneral.getId();
        result.articleEntity = ArticleEntity.from(scheduleGeneral.getArticle());
        result.visitedDate = scheduleGeneral.getVisitedDate();
        result.visitedTime = scheduleGeneral.getVisitedTime();
        result.sortOrder = scheduleGeneral.getSortOrder();
        result.category = scheduleGeneral.getCategory();
        result.durationTime = scheduleGeneral.getDurationTime();
        result.expense = scheduleGeneral.getExpense();
        result.memo = scheduleGeneral.getMemo();
        result.status = scheduleGeneral.getStatus();
        result.dtype = scheduleGeneral.getDtype();
        result.placeName = scheduleGeneral.getPlaceName();
        result.googleMapPlaceId = scheduleGeneral.getGoogleMapPlaceId();
        result.googleMapLatitude = scheduleGeneral.getGoogleMapLatitude();
        result.googleMapLongitude = scheduleGeneral.getGoogleMapLongitude();
        result.googleMapAddress = scheduleGeneral.getGoogleMapAddress();
        result.googleMapPhoneNumber = scheduleGeneral.getGoogleMapPhoneNumber();
        result.googleMapHomePageUrl = scheduleGeneral.getGoogleMapHomePageUrl();
        return result;
    }

    @Override
    public ScheduleGeneral toModel() {
        return ScheduleGeneral.builder()
            .id(this.getId())
            .article(this.articleEntity.toModel())
            .visitedDate(visitedDate)
            .visitedTime(visitedTime)
            .sortOrder(sortOrder)
            .category(this.category)
            .durationTime(this.durationTime)
            .expense(this.expense)
            .memo(this.memo)
            .status(this.status)
            .dtype(this.dtype)
            .placeName(this.placeName)
            .googleMapPlaceId(this.googleMapPlaceId)
            .googleMapLatitude(this.googleMapLatitude)
            .googleMapLongitude(this.googleMapLongitude)
            .googleMapAddress(this.googleMapAddress)
            .googleMapPhoneNumber(this.googleMapPhoneNumber)
            .googleMapHomePageUrl(this.googleMapHomePageUrl)
            .build();
    }

    public void update(ScheduleGeneralRequest request) {
        this.placeName = request.placeName();
        this.googleMapPlaceId = request.googleMapPlaceId();
        this.googleMapLatitude = request.googleMapLatitude();
        this.googleMapLongitude = request.googleMapLongitude();
        this.googleMapAddress = request.googleMapAddress();
        this.googleMapPhoneNumber = request.googleMapPhoneNumber();
        this.googleMapHomePageUrl = request.googleMapHomePageUrl();
    }
}
