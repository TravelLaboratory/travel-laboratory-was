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
import site.travellaboratory.be.controller.articleschedule.dto.ScheduleEtcRequest;
import site.travellaboratory.be.domain.article.Article;
import site.travellaboratory.be.domain.articleschedule.ArticleSchedule;
import site.travellaboratory.be.domain.articleschedule.ArticleScheduleStatus;

@Entity
@DiscriminatorValue("ETC")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleEtc extends ArticleSchedule {
    @Column(nullable = false, length = 255)
    private String placeName;

    private ScheduleEtc(
        // super
        Article article,
        LocalDate visitedDate,
        Time visitedTime,
        Integer sort_order,
        String category,
        ArticleScheduleStatus status,

        String placeName) {
        super(article, visitedDate, visitedTime, sort_order, category,status);
        this.placeName = placeName;
    }

    public static ScheduleEtc of(
        Article article,
        LocalDate visitedDate,
        Time visitedTime,
        Integer sort_order,
        String category,
        ArticleScheduleStatus status,


        ScheduleEtcRequest request
        ) {
        return new ScheduleEtc(
            article,
            visitedDate,
            visitedTime,
            sort_order,
            category,
            status,

            request.placeName());
    }
}
