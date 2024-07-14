package site.travellaboratory.be.domain.review;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import site.travellaboratory.be.domain.review.enums.ReviewLikeStatus;
import site.travellaboratory.be.domain.user.user.User;

@Getter
@Builder
@RequiredArgsConstructor
public class ReviewLike {

    private final Long id;
    private final User user;
    private final Review review;
    private final ReviewLikeStatus status;

    public static ReviewLike create(User user, Review review) {
        return ReviewLike.builder()
            .user(user)
            .review(review)
            .status(ReviewLikeStatus.ACTIVE)
            .build();
    }

    public ReviewLike withToggleStatus() {
        return ReviewLike.builder()
            .id(this.getId())
            .user(this.getUser())
            .review(this.getReview())
            .status((this.status == ReviewLikeStatus.ACTIVE) ? ReviewLikeStatus.INACTIVE
                : ReviewLikeStatus.ACTIVE)
            .build();
    }
}
