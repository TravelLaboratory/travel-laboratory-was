package site.travellaboratory.be.review.domain;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import site.travellaboratory.be.review.domain.enums.ReviewLikeStatus;
import site.travellaboratory.be.user.domain.User;

@Getter
@Builder
@RequiredArgsConstructor
public class ReviewLike {

    private final Long id;
    private final User user;
    private final Review review;
    private final ReviewLikeStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static ReviewLike create(User user, Review review) {
        return ReviewLike.builder()
            .user(user)
            .review(review)
            .status(ReviewLikeStatus.ACTIVE)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    public ReviewLike withToggleStatus() {
        return ReviewLike.builder()
            .id(this.getId())
            .user(this.getUser())
            .review(this.getReview())
            .status((this.status == ReviewLikeStatus.ACTIVE) ? ReviewLikeStatus.INACTIVE
                : ReviewLikeStatus.ACTIVE)
            .createdAt(this.createdAt)
            .updatedAt(LocalDateTime.now())
            .build();
    }
}
