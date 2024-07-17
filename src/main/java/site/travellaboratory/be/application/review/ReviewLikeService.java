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
import site.travellaboratory.be.infrastructure.domains.review.entity.ReviewLikeEntity;
import site.travellaboratory.be.infrastructure.domains.user.UserJpaRepository;
import site.travellaboratory.be.infrastructure.domains.user.entity.UserEntity;
import site.travellaboratory.be.domain.user.enums.UserStatus;

@Service
@RequiredArgsConstructor
public class ReviewLikeService {

    private final ReviewJpaRepository reviewJpaRepository;
    private final ReviewLikeJpaRepository reviewLikeJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Transactional
    public ReviewLikeStatus toggleLikeReview(Long userId, Long reviewId) {
        // 유효하지 않은 후기를 좋아요 할 경우
        Review review = reviewJpaRepository.findByIdAndStatusIn(reviewId,
                List.of(ReviewStatus.ACTIVE, ReviewStatus.PRIVATE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.REVIEW_LIKE_INVALID,
                HttpStatus.NOT_FOUND)).toModel();

        ReviewLikeEntity reviewLikeEntity = reviewLikeJpaRepository.findByUserIdAndReviewId(userId,
                reviewId)
            .orElse(null);

        // 좋아요 누른 유저 가져오기
        UserEntity userEntity = userJpaRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        ReviewLike reviewLike;
        if (reviewLikeEntity != null) {
            reviewLike = reviewLikeEntity.toModel().withToggleStatus();
        } else {
            reviewLike = ReviewLike.create(userEntity.toModel(), review);
        }
        ReviewLike saveReviewLike = reviewLikeJpaRepository.save(ReviewLikeEntity.from(reviewLike)).toModel();
        return saveReviewLike.getStatus();
    }
}
