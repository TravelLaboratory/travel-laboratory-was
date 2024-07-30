package site.travellaboratory.be.review.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.review.application.port.ReviewRepository;
import site.travellaboratory.be.review.domain.Review;
import site.travellaboratory.be.review.domain.ReviewLike;
import site.travellaboratory.be.review.domain.enums.ReviewLikeStatus;
import site.travellaboratory.be.review.domain.enums.ReviewStatus;
import site.travellaboratory.be.review.infrastructure.persistence.entity.ReviewLikeEntity;
import site.travellaboratory.be.review.infrastructure.persistence.repository.ReviewLikeJpaRepository;
import site.travellaboratory.be.user.application.port.UserRepository;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain.enums.UserStatus;

@Service
@RequiredArgsConstructor
public class ReviewLikeService {

    private final ReviewRepository reviewRepository;
    private final ReviewLikeJpaRepository reviewLikeJpaRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReviewLikeStatus toggleLikeReview(Long userId, Long reviewId) {
        // 유효하지 않은 후기를 좋아요 할 경우
        Review review = getReviewById(reviewId);

        ReviewLikeEntity reviewLikeEntity = reviewLikeJpaRepository.findByUserIdAndReviewId(userId,
                reviewId)
            .orElse(null);

        // 좋아요 누른 유저 가져오기
        User user = getUserById(userId);

        ReviewLike reviewLike;
        if (reviewLikeEntity != null) {
            reviewLike = reviewLikeEntity.toModel().withToggleStatus();
        } else {
            reviewLike = ReviewLike.create(user, review);
        }
        ReviewLike saveReviewLike = reviewLikeJpaRepository.save(ReviewLikeEntity.from(reviewLike)).toModel();
        return saveReviewLike.getStatus();
    }

    private User getUserById(Long userId) {
        return userRepository.getByIdAndStatus(userId, UserStatus.ACTIVE);
    }

    private Review getReviewById(Long reviewId) {
        return reviewRepository.findByIdAndStatusIn(reviewId,
                List.of(ReviewStatus.ACTIVE, ReviewStatus.PRIVATE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.REVIEW_LIKE_INVALID,
                HttpStatus.NOT_FOUND));
    }
}
