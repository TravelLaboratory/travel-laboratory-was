package site.travellaboratory.be.infrastructure.domains.review.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.travellaboratory.be.domain.review.Review;
import site.travellaboratory.be.domain.review.enums.ReviewStatus;
import site.travellaboratory.be.infrastructure.common.BaseJpaEntity;
import site.travellaboratory.be.infrastructure.domains.article.entity.ArticleJpaEntity;
import site.travellaboratory.be.infrastructure.domains.user.entity.UserJpaEntity;

@Entity
@Table(name = "review")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewJpaEntity extends BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserJpaEntity userJpaEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    // todo : 여행 계획의 status 만 확인하면 되기에 어찌보면 굳이 Article로 객체를 가지고 있을 필요가 없을 수 있다.
    private ArticleJpaEntity articleJpaEntity;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 255)
    private String representativeImgUrl;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewStatus status;

    public static ReviewJpaEntity from(Review review) {
        ReviewJpaEntity result = new ReviewJpaEntity();
        result.id = review.getId();
        result.userJpaEntity = UserJpaEntity.from(review.getUser());
        result.articleJpaEntity = ArticleJpaEntity.from(review.getArticle());
        result.title = review.getTitle();
        result.representativeImgUrl = review.getRepresentativeImgUrl();
        result.description = review.getDescription();
        result.status = review.getStatus();
        return result;
    }

    public Review toModel() {
        return Review.builder()
            .id(this.id)
            .user(this.userJpaEntity.toModel())
            .article(this.articleJpaEntity.toModel())
            .title(this.title)
            .representativeImgUrl(this.representativeImgUrl)
            .description(this.description)
            .status(this.status)
            .build();
    }
}
