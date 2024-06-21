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
import site.travellaboratory.be.controller.articleschedule.dto.ScheduleGeneralRequest;
import site.travellaboratory.be.domain.article.Article;
import site.travellaboratory.be.domain.articleschedule.ArticleSchedule;
import site.travellaboratory.be.domain.articleschedule.ArticleScheduleStatus;

@Entity
@DiscriminatorValue("GENERAL")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleGeneral extends ArticleSchedule {

    @Column(nullable = false, length = 255)
    private String placeName;

    @Column(nullable = false, columnDefinition = "TIME(4)")
    private Time durationTime;

    @Column(nullable = false, length = 15)
    private String expense;

    @Column(length = 500)
    private String memo;

    @Column(nullable = false)
    private Long googleMapPlaceId;

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

    private ScheduleGeneral(
        // super
        Article article,
        LocalDate visitedDate,
        Time visitedTime,
        Integer sort_order,
        String category,
        ArticleScheduleStatus status,

        String placeName,
        Time durationTime,
        String expense,
        String memo,
        Long googleMapPlaceId,
        Double googleMapLatitude,
        Double googleMapLongitude,
        String googleMapAddress,
        String googleMapPhoneNumber,
        String googleMapHomePageUrl
        ) {
        super(article, visitedDate, visitedTime, sort_order, category, status);
        this.placeName = placeName;
        this.durationTime = durationTime;
        this.expense = expense;
        this.memo = memo;
        this.googleMapPlaceId = googleMapPlaceId;
        this.googleMapLatitude = googleMapLatitude;
        this.googleMapLongitude = googleMapLongitude;
        this.googleMapAddress = googleMapAddress;
        this.googleMapPhoneNumber = googleMapPhoneNumber;
        this.googleMapHomePageUrl = googleMapHomePageUrl;
    }

    public static ScheduleGeneral of(
        Article article,
        LocalDate visitedDate,
        Time visitedTime,
        Integer sort_order,
        String category,
        ArticleScheduleStatus status,

        ScheduleGeneralRequest request
    ) {
        return new ScheduleGeneral(
            article,
            visitedDate,
            visitedTime,
            sort_order,
            category,
            status,

            request.placeName(),
            request.durationTime(),
            request.expense(),
            request.memo(),
            request.googleMapPlaceId(),
            request.googleMapLatitude(),
            request.googleMapLongitude(),
            request.googleMapAddress(),
            request.googleMapPhoneNumber(),
            request.googleMapHomePageUrl()
        );
    }
}
