package site.travellaboratory.be.domain.articleschedule.dtype;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import java.sql.Time;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.travellaboratory.be.controller.articleschedule.dto.ScheduleTransportRequest;
import site.travellaboratory.be.domain.article.Article;
import site.travellaboratory.be.domain.articleschedule.ArticleSchedule;
import site.travellaboratory.be.domain.articleschedule.ArticleScheduleStatus;

@Entity
@DiscriminatorValue("TRANSPORT")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleTransport extends ArticleSchedule {

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

    private ScheduleTransport(
        // super
        Article article,
        LocalDate visitedDate,
        Time visitedTime,
        Integer sort_order,
        String category,
        ArticleScheduleStatus status,


        String transportation,
        String startPlaceName,
        String googleMapStartPlaceAddress,
        Double googleMapStartLatitude,
        Double googleMapStartLongitude,
        String endPlaceName,
        String googleMapEndPlaceAddress,
        Double googleMapEndLatitude,
        Double googleMapEndLongitude) {
        super(article, visitedDate, visitedTime, sort_order, category, status);
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

    public static ScheduleTransport of(
        Article article,
        LocalDate visitedDate,
        Time visitedTime,
        Integer sort_order,
        String category,
        ArticleScheduleStatus status,

        ScheduleTransportRequest request
    ) {
        return new ScheduleTransport(
            article,
            visitedDate,
            visitedTime,
            sort_order,
            category,
            status,

            request.transportation(),
            request.startPlaceName(),
            request.googleMapStartPlaceAddress(),
            request.googleMapStartLatitude(),
            request.googleMapStartLongitude(),
            request.endPlaceName(),
            request.googleMapEndPlaceAddress(),
            request.googleMapEndLatitude(),
            request.googleMapEndLongitude()
        );
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
