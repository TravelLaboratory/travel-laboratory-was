package site.travellaboratory.be.domain.articleschedule;

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
import site.travellaboratory.be.controller.articleschedule.dto.put.ArticleScheduleRequest;
import site.travellaboratory.be.domain.BaseEntity;
import site.travellaboratory.be.domain.article.Article;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // 조인 전략
@DiscriminatorColumn(name = "dtype") // 조인 전략은 default DTYPE을 만들지 않기에 명시
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ArticleSchedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @Column(nullable = false)
    private LocalDate visitedDate;

    @Column(nullable = false, columnDefinition = "TIME(4)")
    private Time visitedTime;

    @Column(nullable = false, columnDefinition = "TINYINT")
    private Integer sortOrder;

    @Column(nullable = false, length = 15)
    private String category;

    @Column(nullable = false, columnDefinition = "TIME(4)")
    private Time durationTime;

    @Column(nullable = false, length = 15)
    private String expense;

    @Column(length = 500)
    private String memo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArticleScheduleStatus status;

    // insertable, updatable false 한 이유 : JPA 알아서 처리, 직접 조작하지 못하게 막기 위해
    @Column(name = "dtype", nullable = false, insertable = false, updatable = false)
    private String dtype;

    protected ArticleSchedule(
        Article article,
        LocalDate visitedDate,
        Time visitedTime,
        Integer sortOrder,
        String category,
        Time durationTime,
        String expense,
        String memo,
        ArticleScheduleStatus status) {
        this.article = article;
        this.visitedDate = visitedDate;
        this.visitedTime = visitedTime;
        this.sortOrder = sortOrder;
        this.category = category;
        this.durationTime = durationTime;
        this.expense = expense;
        this.memo = memo;
        this.status = status;
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
