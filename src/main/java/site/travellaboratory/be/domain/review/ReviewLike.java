package site.travellaboratory.be.domain.review;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import site.travellaboratory.be.domain.review.enums.ReviewLikeStatus;
import site.travellaboratory.be.infrastructure.domains.user.entity.UserJpaEntity;

@Getter
@Builder
@RequiredArgsConstructor
public class ReviewLike {

    private final Long id;
    private final UserJpaEntity userJpaEntity;
    private final Review review;
    private final ReviewLikeStatus status;

    public static ReviewLike create(UserJpaEntity userJpaEntity, Review review) {
        return ReviewLike.builder()
            .userJpaEntity(userJpaEntity)
            .review(review)
            .status(ReviewLikeStatus.ACTIVE)
            .build();
    }

    public ReviewLike withToggleStatus() {
        return ReviewLike.builder()
            .id(this.getId())
            .userJpaEntity(this.getUserJpaEntity())
            .review(this.getReview())
            .status((this.status == ReviewLikeStatus.ACTIVE) ? ReviewLikeStatus.INACTIVE
                : ReviewLikeStatus.ACTIVE)
            .build();
    }
}
