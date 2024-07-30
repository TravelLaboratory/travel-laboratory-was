package site.travellaboratory.be.review.application.port;

import java.util.Optional;
import site.travellaboratory.be.review.domain.ReviewLike;
import site.travellaboratory.be.review.domain.enums.ReviewLikeStatus;

public interface ReviewLikeRepository  {

    Optional<ReviewLike> findByUserIdAndReviewId(Long userId, Long reviewJpaEntityId);

    Long countByReviewIdAndStatus(Long reviewId, ReviewLikeStatus status);

    ReviewLike save(ReviewLike reviewLike);
}
