package site.travellaboratory.be.review.infrastructure.persistence.entity;

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
import site.travellaboratory.be.review.domain.Review;
import site.travellaboratory.be.review.domain.enums.ReviewStatus;
import site.travellaboratory.be.common.infrastructure.common.BaseEntity;
import site.travellaboratory.be.article.infrastructure.persistence.entity.ArticleEntity;
import site.travellaboratory.be.user.infrastructure.persistence.entity.UserEntity;

@Entity
@Table(name = "review")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    // todo : 여행 계획의 status 만 확인하면 되기에 어찌보면 굳이 Article로 객체를 가지고 있을 필요가 없을 수 있다.
    private ArticleEntity articleEntity;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 255)
    private String representativeImgUrl;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewStatus status;

    public static ReviewEntity from(Review review) {
        ReviewEntity result = new ReviewEntity();
        result.id = review.getId();
        result.userEntity = UserEntity.from(review.getUser());
        result.articleEntity = ArticleEntity.from(review.getArticle());
        result.title = review.getTitle();
        result.representativeImgUrl = review.getRepresentativeImgUrl();
        result.description = review.getDescription();
        result.status = review.getStatus();
        result.setCreatedAt(review.getCreatedAt());
        result.setUpdatedAt(review.getUpdatedAt());
        return result;
    }

    public Review toModel() {
        return Review.builder()
            .id(this.id)
            .user(this.userEntity.toModel())
            .article(this.articleEntity.toModel())
            .title(this.title)
            .representativeImgUrl(this.representativeImgUrl)
            .description(this.description)
            .status(this.status)
            .createdAt(this.getCreatedAt())
            .updatedAt(this.getUpdatedAt())
            .build();
    }
}
