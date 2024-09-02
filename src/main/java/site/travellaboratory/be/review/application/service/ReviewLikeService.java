package site.travellaboratory.be.review.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.presentation.error.ErrorCodes;
import site.travellaboratory.be.common.presentation.exception.BeApplicationException;
import site.travellaboratory.be.review.application.port.ReviewLikeRepository;
import site.travellaboratory.be.review.application.port.ReviewRepository;
import site.travellaboratory.be.review.domain.Review;
import site.travellaboratory.be.review.domain.ReviewLike;
import site.travellaboratory.be.review.domain.enums.ReviewStatus;
import site.travellaboratory.be.user.application.port.UserRepository;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain.enums.UserStatus;

@Service
@RequiredArgsConstructor
public class ReviewLikeService {

    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReviewLike toggleLike(Long userId, Long reviewId) {
        // 유효하지 않은 후기를 좋아요 할 경우
        Review review = getReviewById(reviewId);

        ReviewLike reviewLike = reviewLikeRepository.findByUserIdAndReviewId(userId,
                reviewId)
            .orElse(null);

        // 좋아요 누른 유저 가져오기
        User user = getUserById(userId);

        if (reviewLike != null) {
            reviewLike = reviewLike.withToggleStatus();
        } else {
            reviewLike = ReviewLike.create(user, review);
        }
        return reviewLikeRepository.save(reviewLike);
    }

    private User getUserById(Long userId) {
        return userRepository.getByIdAndStatus(userId, UserStatus.ACTIVE);
    }

    private Review getReviewById(Long reviewId) {
        return reviewRepository.findByIdAndStatus(reviewId, ReviewStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.REVIEW_LIKE_INVALID_REVIEW_ID,
                HttpStatus.NOT_FOUND));
    }
}
