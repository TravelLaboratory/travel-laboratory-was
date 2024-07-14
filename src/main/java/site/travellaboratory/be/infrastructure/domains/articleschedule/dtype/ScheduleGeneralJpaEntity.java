package site.travellaboratory.be.infrastructure.domains.articleschedule.dtype;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import java.sql.Time;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.travellaboratory.be.infrastructure.domains.article.entity.ArticleJpaEntity;
import site.travellaboratory.be.presentation.articleschedule.dto.writer.ScheduleGeneralRequest;
import site.travellaboratory.be.infrastructure.domains.articleschedule.ArticleScheduleJpaEntity;
import site.travellaboratory.be.infrastructure.domains.articleschedule.ArticleScheduleStatus;

@Entity
@Table(name = "schedule_general")
@DiscriminatorValue("GENERAL")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleGeneralJpaEntity extends ArticleScheduleJpaEntity {

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

    private ScheduleGeneralJpaEntity(
        // super
        ArticleJpaEntity articleJpaEntity,
        LocalDate visitedDate,
        Time visitedTime,
        Integer sortOrder,
        String category,
        Time durationTime,
        String expense,
        String memo,
        ArticleScheduleStatus status,

        String placeName,
        String googleMapPlaceId,
        Double googleMapLatitude,
        Double googleMapLongitude,
        String googleMapAddress,
        String googleMapPhoneNumber,
        String googleMapHomePageUrl
        ) {
        super(articleJpaEntity, visitedDate, visitedTime, sortOrder, category, durationTime, expense, memo, status);
        this.placeName = placeName;
        this.googleMapPlaceId = googleMapPlaceId;
        this.googleMapLatitude = googleMapLatitude;
        this.googleMapLongitude = googleMapLongitude;
        this.googleMapAddress = googleMapAddress;
        this.googleMapPhoneNumber = googleMapPhoneNumber;
        this.googleMapHomePageUrl = googleMapHomePageUrl;
    }

    public static ScheduleGeneralJpaEntity of(
        ArticleJpaEntity articleJpaEntity,
        LocalDate visitedDate,
        Time visitedTime,
        Integer sortOrder,
        String category,
        Time durationTime,
        String expense,
        String memo,
        ArticleScheduleStatus status,

        ScheduleGeneralRequest request
    ) {
        return new ScheduleGeneralJpaEntity(
            articleJpaEntity,
            visitedDate,
            visitedTime,
            sortOrder,
            category,
            durationTime,
            expense,
            memo,
            status,

            request.placeName(),
            request.googleMapPlaceId(),
            request.googleMapLatitude(),
            request.googleMapLongitude(),
            request.googleMapAddress(),
            request.googleMapPhoneNumber(),
            request.googleMapHomePageUrl()
        );
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