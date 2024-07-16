package site.travellaboratory.be.infrastructure.domains.articleschedule.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.travellaboratory.be.domain.article.ScheduleEtc;
import site.travellaboratory.be.infrastructure.domains.article.entity.ArticleJpaEntity;
import site.travellaboratory.be.presentation.articleschedule.dto.writer.ScheduleEtcRequest;

@Entity
@Table(name = "schedule_etc")
@DiscriminatorValue("ETC")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleEtcJpaEntity extends ArticleScheduleJpaEntity {

    @Column(nullable = false, length = 255)
    private String placeName;

    public static ScheduleEtcJpaEntity from(ScheduleEtc scheduleEtc) {
        ScheduleEtcJpaEntity result = new ScheduleEtcJpaEntity();
        result.id = scheduleEtc.getId();
        result.articleJpaEntity = ArticleJpaEntity.from(scheduleEtc.getArticle());
        result.visitedDate = scheduleEtc.getVisitedDate();
        result.visitedTime = scheduleEtc.getVisitedTime();
        result.sortOrder = scheduleEtc.getSortOrder();
        result.category = scheduleEtc.getCategory();
        result.durationTime = scheduleEtc.getDurationTime();
        result.expense = scheduleEtc.getExpense();
        result.memo = scheduleEtc.getMemo();
        result.status = scheduleEtc.getStatus();
        result.dtype = scheduleEtc.getDtype();
        result.placeName = scheduleEtc.getPlaceName();
        return result;
    }

    @Override
    public ScheduleEtc toModel() {
        return ScheduleEtc.builder()
            .id(this.getId())
            .article(this.articleJpaEntity.toModel())
            .visitedDate(visitedDate)
            .visitedTime(visitedTime)
            .sortOrder(sortOrder)
            .category(this.category)
            .durationTime(this.durationTime)
            .expense(this.expense)
            .memo(this.memo)
            .status(this.status)
            .dtype(this.dtype)
            .placeName(this.placeName)
            .build();
    }

    public void update(ScheduleEtcRequest request) {
        this.placeName = request.placeName();
    }
}
