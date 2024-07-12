package site.travellaboratory.be.application.review;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.domain.review.Review;
import site.travellaboratory.be.domain.review.ReviewLike;
import site.travellaboratory.be.domain.review.enums.ReviewLikeStatus;
import site.travellaboratory.be.domain.review.enums.ReviewStatus;
import site.travellaboratory.be.infrastructure.domains.review.repository.ReviewJpaRepository;
import site.travellaboratory.be.infrastructure.domains.review.repository.ReviewLikeJpaRepository;
import site.travellaboratory.be.infrastructure.domains.review.entity.ReviewLikeJpaEntity;

@Service
@RequiredArgsConstructor
public class ReviewLikeService {

    private final ReviewJpaRepository reviewJpaRepository;
    private final ReviewLikeJpaRepository reviewLikeJpaRepository;

    @Transactional
    public ReviewLikeStatus toggleLikeReview(Long userId, Long reviewId) {
        // 유효하지 않은 후기를 좋아요 할 경우
        Review review = reviewJpaRepository.findByIdAndStatusIn(reviewId,
                List.of(ReviewStatus.ACTIVE, ReviewStatus.PRIVATE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.REVIEW_LIKE_INVALID,
                HttpStatus.NOT_FOUND)).toModel();

        ReviewLikeJpaEntity reviewLikeJpaEntity = reviewLikeJpaRepository.findByUserIdAndReviewId(userId,
                reviewId)
            .orElse(null);

        ReviewLike reviewLike;
        if (reviewLikeJpaEntity != null) {
            reviewLike = reviewLikeJpaEntity.toModel().withToggleStatus();
        } else {
            reviewLike = ReviewLike.create(review.getUser(), review);
        }
        ReviewLikeJpaEntity saveReviewLike = reviewLikeJpaRepository.save(ReviewLikeJpaEntity.from(reviewLike));
        return saveReviewLike.getStatus();
    }
}
