package site.travellaboratory.be.review.application.port;

import java.util.Optional;
import site.travellaboratory.be.review.domain.ReviewLike;

public interface ReviewLikeRepository  {

    Optional<ReviewLike> findByUserIdAndReviewId(Long userId, Long reviewJpaEntityId);

    ReviewLike save(ReviewLike reviewLike);
}
