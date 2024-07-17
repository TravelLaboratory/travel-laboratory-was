package site.travellaboratory.be.article.infrastructure.persistence.entity._schedule;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.travellaboratory.be.article.domain._schedule.ScheduleTransport;
import site.travellaboratory.be.article.infrastructure.persistence.entity.ArticleEntity;
import site.travellaboratory.be.article.presentation.response._schedule.writer.ScheduleTransportRequest;

@Entity
@Table(name = "schedule_transport")
@DiscriminatorValue("TRANSPORT")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleTransportEntity extends ArticleScheduleEntity {

    @Column(nullable = false, length = 20)
    private String transportation;

    @Column(nullable = false, length = 255)
    private String startPlaceName;

    @Column(nullable = false, length = 500)
    private String googleMapStartPlaceAddress;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,8)")
    private Double googleMapStartLatitude;

    @Column(nullable = false, columnDefinition = "DECIMAL(11,8)")
    private Double googleMapStartLongitude;

    @Column(nullable = false, length = 255)
    private String endPlaceName;

    @Column(nullable = false, length = 500)
    private String googleMapEndPlaceAddress;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,8)")
    private Double googleMapEndLatitude;

    @Column(nullable = false, columnDefinition = "DECIMAL(11,8)")
    private Double googleMapEndLongitude;

    public static ScheduleTransportEntity from(ScheduleTransport scheduleTransport) {
        ScheduleTransportEntity result = new ScheduleTransportEntity();
        result.id = scheduleTransport.getId();
        result.articleEntity = ArticleEntity.from(scheduleTransport.getArticle());
        result.visitedDate = scheduleTransport.getVisitedDate();
        result.visitedTime = scheduleTransport.getVisitedTime();
        result.sortOrder = scheduleTransport.getSortOrder();
        result.category = scheduleTransport.getCategory();
        result.durationTime = scheduleTransport.getDurationTime();
        result.expense = scheduleTransport.getExpense();
        result.memo = scheduleTransport.getMemo();
        result.status = scheduleTransport.getStatus();
        result.dtype = scheduleTransport.getDtype();
        result.transportation = scheduleTransport.getTransportation();
        result.startPlaceName = scheduleTransport.getStartPlaceName();
        result.googleMapStartPlaceAddress = scheduleTransport.getGoogleMapStartPlaceAddress();
        result.googleMapStartLatitude = scheduleTransport.getGoogleMapStartLatitude();
        result.googleMapStartLongitude = scheduleTransport.getGoogleMapStartLongitude();
        result.endPlaceName = scheduleTransport.getEndPlaceName();
        result.googleMapEndPlaceAddress = scheduleTransport.getGoogleMapEndPlaceAddress();
        result.googleMapEndLatitude = scheduleTransport.getGoogleMapEndLatitude();
        result.googleMapEndLongitude = scheduleTransport.getGoogleMapEndLongitude();
        return result;
    }

    @Override
    public ScheduleTransport toModel() {
        return ScheduleTransport.builder()
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

    public void update(ScheduleTransportRequest request) {
        this.transportation = request.transportation();
        this.startPlaceName = request.startPlaceName();
        this.googleMapStartPlaceAddress = request.googleMapStartPlaceAddress();
        this.googleMapStartLatitude = request.googleMapStartLatitude();
        this.googleMapStartLongitude = request.googleMapStartLongitude();
        this.endPlaceName = request.endPlaceName();
        this.googleMapEndPlaceAddress = request.googleMapEndPlaceAddress();
        this.googleMapEndLatitude = request.googleMapEndLatitude();
        this.googleMapEndLongitude = request.googleMapEndLongitude();
    }
}
