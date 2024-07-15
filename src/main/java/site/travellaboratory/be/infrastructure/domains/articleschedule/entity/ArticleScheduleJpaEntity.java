package site.travellaboratory.be.infrastructure.domains.articleschedule.entity;

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
import java.sql.Time;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.travellaboratory.be.domain.article.ArticleSchedule;
import site.travellaboratory.be.infrastructure.common.BaseEntity;
import site.travellaboratory.be.infrastructure.domains.article.entity.ArticleJpaEntity;
import site.travellaboratory.be.domain.article.enums.ArticleScheduleStatus;
import site.travellaboratory.be.presentation.articleschedule.dto.writer.ArticleScheduleRequest;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // 조인 전략
@DiscriminatorColumn(name = "dtype") // 조인 전략은 default DTYPE을 만들지 않기에 명시
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ArticleScheduleJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    protected ArticleJpaEntity articleJpaEntity;

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

    protected static <T extends ArticleScheduleJpaEntity> T from(ArticleSchedule model, T entity) {
        entity.id = model.getId();
        entity.articleJpaEntity = ArticleJpaEntity.from(model.getArticle());
        entity.visitedDate = model.getVisitedDate();
        entity.visitedTime = model.getVisitedTime();
        entity.sortOrder = model.getSortOrder();
        entity.category = model.getCategory();
        entity.durationTime = model.getDurationTime();
        entity.expense = model.getExpense();
        entity.memo = model.getMemo();
        entity.status = model.getStatus();
        entity.dtype = model.getDtype();
        return entity;
    }

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
