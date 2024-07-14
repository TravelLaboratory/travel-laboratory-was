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
import site.travellaboratory.be.presentation.articleschedule.dto.writer.ScheduleEtcRequest;
import site.travellaboratory.be.infrastructure.domains.articleschedule.ArticleScheduleJpaEntity;
import site.travellaboratory.be.infrastructure.domains.articleschedule.ArticleScheduleStatus;

@Entity
@Table(name = "schedule_etc")
@DiscriminatorValue("ETC")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleEtcJpaEntity extends ArticleScheduleJpaEntity {
    @Column(nullable = false, length = 255)
    private String placeName;

    private ScheduleEtcJpaEntity(
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
        String placeName) {
        super(articleJpaEntity, visitedDate, visitedTime, sortOrder, category, durationTime, expense, memo, status);
        this.placeName = placeName;
    }

    public static ScheduleEtcJpaEntity of(
        ArticleJpaEntity articleJpaEntity,
        LocalDate visitedDate,
        Time visitedTime,
        Integer sortOrder,
        String category,
        Time durationTime,
        String expense,
        String memo,
        ArticleScheduleStatus status,
        ScheduleEtcRequest request
        ) {
        return new ScheduleEtcJpaEntity(
            articleJpaEntity,
            visitedDate,
            visitedTime,
            sortOrder,
            category,
            durationTime,
            expense,
            memo,
            status,
            request.placeName());
    }

    public void update(ScheduleEtcRequest request) {
        this.placeName = request.placeName();
    }
}
