package site.travellaboratory.be.domain.articleschedule;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import java.time.LocalDate;
import java.time.LocalTime;
import site.travellaboratory.be.domain.BaseEntity;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // 조인 전략
@DiscriminatorColumn(name = "dtype") // 조인 전략은 default DTYPE을 만들지 않기에 명시
public abstract class ArticleSchedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long articleId;

    @Column(nullable = false)
    private LocalDate visitedDate;

    @Column(nullable = false)
    private LocalTime visitedTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArticleScheduleStatus status;

    // insertable, updatable false 한 이유 : JPA 알아서 처리, 직접 조작하지 못하게 막기 위해
    @Column(name = "dtype", nullable = false, insertable = false, updatable = false)
    private String dtype;
}
