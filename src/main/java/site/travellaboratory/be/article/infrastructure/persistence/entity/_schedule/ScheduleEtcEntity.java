package site.travellaboratory.be.article.infrastructure.persistence.entity._schedule;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.travellaboratory.be.article.domain._schedule.ScheduleEtc;
import site.travellaboratory.be.article.infrastructure.persistence.entity.ArticleEntity;

@Entity
@Table(name = "schedule_etc")
@DiscriminatorValue("ETC")
@PrimaryKeyJoinColumn(name = "schedule_id")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleEtcEntity extends ArticleScheduleEntity {

    @Column(nullable = false, length = 255)
    private String placeName;

    public static ScheduleEtcEntity from(ScheduleEtc scheduleEtc) {
        ScheduleEtcEntity result = new ScheduleEtcEntity();
        result.id = scheduleEtc.getId();
        result.articleEntity = ArticleEntity.from(scheduleEtc.getArticle());
        result.visitedDate = scheduleEtc.getVisitedDate();
        result.visitedTime = scheduleEtc.getVisitedTime();
        result.sortOrder = scheduleEtc.getSortOrder();
        result.category = scheduleEtc.getCategory();
        result.durationTime = scheduleEtc.getDurationTime();
        result.expense = scheduleEtc.getExpense();
        result.memo = scheduleEtc.getMemo();
        result.status = scheduleEtc.getStatus();
        result.dtype = scheduleEtc.getDtype();
        result.setCreatedAt(scheduleEtc.getCreatedAt());
        result.setUpdatedAt(scheduleEtc.getUpdatedAt());
        result.placeName = scheduleEtc.getPlaceName();
        return result;
    }

    @Override
    public ScheduleEtc toModel() {
        return ScheduleEtc.builder()
            .id(this.getId())
            .article(this.articleEntity.toModel())
            .visitedDate(visitedDate)
            .visitedTime(visitedTime)
            .sortOrder(sortOrder)
            .category(this.category)
            .durationTime(this.durationTime)
            .expense(this.expense)
            .memo(this.memo)
            .status(this.status)
            .dtype(this.dtype)
            .createdAt(this.getCreatedAt())
            .updatedAt(this.getUpdatedAt())
            .placeName(this.placeName)
            .build();
    }
}
