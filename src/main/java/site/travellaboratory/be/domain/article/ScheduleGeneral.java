package site.travellaboratory.be.domain.article;

import java.sql.Time;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import site.travellaboratory.be.domain.article.enums.ArticleScheduleStatus;

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
