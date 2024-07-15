package site.travellaboratory.be.domain.article;

import java.sql.Time;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import site.travellaboratory.be.domain.article.enums.ArticleScheduleStatus;

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
