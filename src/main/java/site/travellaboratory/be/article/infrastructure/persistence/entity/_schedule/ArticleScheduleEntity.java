package site.travellaboratory.be.article.infrastructure.persistence.entity._schedule;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.sql.Time;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.travellaboratory.be.article.domain._schedule.ArticleSchedule;
import site.travellaboratory.be.article.domain._schedule.ScheduleEtc;
import site.travellaboratory.be.article.domain._schedule.ScheduleGeneral;
import site.travellaboratory.be.article.domain._schedule.ScheduleTransport;
import site.travellaboratory.be.article.domain._schedule.enums.ArticleScheduleStatus;
import site.travellaboratory.be.common.infrastructure.common.BaseEntity;
import site.travellaboratory.be.article.infrastructure.persistence.entity.ArticleEntity;
import site.travellaboratory.be.article.domain._schedule.request.ArticleScheduleRequest;

@Entity
@Table(name = "article_schedule")
@Inheritance(strategy = InheritanceType.JOINED) // 조인 전략
@DiscriminatorColumn(name = "dtype") // 조인 전략은 default DTYPE을 만들지 않기에 명시
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ArticleScheduleEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    protected ArticleEntity articleEntity;

    @Column(nullable = false)
    protected LocalDate visitedDate;

    @Column(nullable = false, columnDefinition = "TIME(4)")
    protected Time visitedTime;

    @Column(nullable = false, columnDefinition = "TINYINT")
    protected Integer sortOrder;

    @Column(nullable = false, length = 15)
    protected String category;

    @Column(nullable = false, columnDefinition = "TIME(4)")
    protected Time durationTime;

    @Column(nullable = false, length = 15)
    protected String expense;

    @Column(length = 500)
    protected String memo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected ArticleScheduleStatus status;

    // insertable, updatable false 한 이유 : JPA 알아서 처리, 직접 조작하지 못하게 막기 위해
    @Column(name = "dtype", nullable = false, insertable = false, updatable = false)
    protected String dtype;

    public static ArticleScheduleEntity from(ArticleSchedule articleSchedule) {
        ArticleScheduleEntity result;
        switch (articleSchedule.getDtype()) {
            case "GENERAL" -> result = ScheduleGeneralEntity.from((ScheduleGeneral) articleSchedule);
            case "TRANSPORT" -> result = ScheduleTransportEntity.from((ScheduleTransport) articleSchedule);
            case "ETC" -> result = ScheduleEtcEntity.from((ScheduleEtc) articleSchedule);
            default -> throw new IllegalArgumentException("Unknown dtype: " + articleSchedule.getDtype());
        }
        return result;
    }

    public abstract ArticleSchedule toModel();

    public void delete() {
        this.status = ArticleScheduleStatus.INACTIVE;
    }

    public void update(ArticleScheduleRequest request) {
        this.visitedDate = request.visitedDate();
        this.visitedTime = request.visitedTime();
        this.sortOrder = request.sortOrder();
        this.category = request.category();
        this.durationTime = request.durationTime();
        this.expense = request.expense();
        this.memo = request.memo();
    }
}
