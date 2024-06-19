package site.travellaboratory.be.domain.articleschedule;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;

@Entity
@DiscriminatorValue("ETC")
@PrimaryKeyJoinColumn(name = "id")
@Getter
public class ArticleScheduleEtc extends ArticleSchedule{
    @Column(nullable = false, length = 255)
    private String placeName;
}
