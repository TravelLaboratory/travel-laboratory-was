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
import site.travellaboratory.be.common.infrastructure.common.BaseEntity;
import site.travellaboratory.be.review.domain.ReviewLike;
import site.travellaboratory.be.review.domain.enums.ReviewLikeStatus;
import site.travellaboratory.be.user.infrastructure.persistence.entity.UserEntity;

@Entity
@Table(name = "review_like")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewLikeEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private ReviewEntity reviewEntity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewLikeStatus status;

    public static ReviewLikeEntity from(ReviewLike reviewLike) {
        ReviewLikeEntity result = new ReviewLikeEntity();
        result.id = reviewLike.getId();
        result.userEntity = UserEntity.from(reviewLike.getUser());
        result.reviewEntity = ReviewEntity.from(reviewLike.getReview());
        result.status = reviewLike.getStatus();
        result.setCreatedAt(reviewLike.getCreatedAt());
        result.setUpdatedAt(reviewLike.getUpdatedAt());
        return result;
    }

    public ReviewLike toModel() {
        return ReviewLike.builder()
            .id(this.getId())
            .user(this.getUserEntity().toModel())
            .review(this.getReviewEntity().toModel())
            .status(this.getStatus())
            .createdAt(this.getCreatedAt())
            .updatedAt(this.getUpdatedAt())
            .build();
    }
}
