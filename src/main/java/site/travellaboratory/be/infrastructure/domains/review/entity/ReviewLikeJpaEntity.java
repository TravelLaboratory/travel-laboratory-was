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
import site.travellaboratory.be.domain.review.ReviewLike;
import site.travellaboratory.be.infrastructure.common.BaseEntity;
import site.travellaboratory.be.infrastructure.domains.user.entity.User;
import site.travellaboratory.be.domain.review.enums.ReviewLikeStatus;

@Entity
@Table(name = "review_like")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewLikeJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private ReviewJpaEntity reviewJpaEntity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewLikeStatus status;

    public static ReviewLikeJpaEntity from(ReviewLike reviewLike) {
        ReviewLikeJpaEntity result = new ReviewLikeJpaEntity();
        result.id = reviewLike.getId();
        result.user = reviewLike.getUser();
        result.reviewJpaEntity = ReviewJpaEntity.from(reviewLike.getReview());
        result.status = reviewLike.getStatus();
        return result;
    }

    public ReviewLike toModel() {
        return ReviewLike.builder()
            .id(this.getId())
            .user(this.getUser())
            .review(this.getReviewJpaEntity().toModel())
            .status(this.getStatus())
            .build();
    }
}
