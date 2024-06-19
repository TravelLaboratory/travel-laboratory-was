package site.travellaboratory.be.domain.articleschedule;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import java.time.LocalTime;
import lombok.Getter;

@Entity
@DiscriminatorValue("GENERAL")
@PrimaryKeyJoinColumn(name = "id")
@Getter
public class ArticleScheduleGeneral extends ArticleSchedule {

    @Column(nullable = false, length = 255)
    private String placeName;

    @Column(nullable = false, columnDefinition = "TIME")
    private LocalTime durationTime;

    @Column(nullable = false, length = 10)
    private String generalCategory;

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
}
