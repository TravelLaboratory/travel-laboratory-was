package site.travellaboratory.be.application.review;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.infrastructure.review.ReviewRepository;
import site.travellaboratory.be.infrastructure.review.entity.Review;
import site.travellaboratory.be.infrastructure.review.enums.ReviewStatus;
import site.travellaboratory.be.infrastructure.user.entity.User;
import site.travellaboratory.be.infrastructure.userlikereview.UserLikeReviewRepository;
import site.travellaboratory.be.infrastructure.userlikereview.entity.UserLikeReview;
import site.travellaboratory.be.presentation.review.dto.userlikereview.ReviewToggleLikeResponse;

@Service
@RequiredArgsConstructor
public class ReviewLikeService {

    private final ReviewRepository reviewRepository;
    private final UserLikeReviewRepository userLikeReviewRepository;

    @Transactional
    public ReviewToggleLikeResponse toggleLikeReview(Long userId, Long reviewId) {
        // 유효하지 않은 후기를 좋아요 할 경우
        Review review = reviewRepository.findByIdAndStatusIn(reviewId,
                List.of(ReviewStatus.ACTIVE, ReviewStatus.PRIVATE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.REVIEW_LIKE_INVALID,
                HttpStatus.NOT_FOUND));

        UserLikeReview userLikeReview = userLikeReviewRepository.findByUserIdAndReviewId(userId,
                reviewId)
            .orElse(null);

        // 처음 좋아요를 누른 게 아닌 경우
        if (userLikeReview != null) {
            userLikeReview.toggleStatus();
        } else {
            // 처음 좋아요를 누른 경우 - 새로 생성
            User user = User.of(userId);
            userLikeReview = UserLikeReview.of(user, review);
        }

        UserLikeReview saveLikeReview = userLikeReviewRepository.save(userLikeReview);
        return ReviewToggleLikeResponse.from(saveLikeReview.getStatus());
    }
}
