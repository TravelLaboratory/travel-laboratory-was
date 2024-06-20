package site.travellaboratory.be.domain.articleschedule.dtype;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import site.travellaboratory.be.domain.articleschedule.ArticleSchedule;

@Entity
@DiscriminatorValue("TRANSPORT")
@PrimaryKeyJoinColumn(name = "id")
@Getter
public class ArticleScheduleTransport extends ArticleSchedule {

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
}
