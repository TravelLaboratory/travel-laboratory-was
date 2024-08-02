package site.travellaboratory.be.review.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.error.ErrorCodes;
import site.travellaboratory.be.review.application.port.ReviewLikeRepository;
import site.travellaboratory.be.review.application.port.ReviewRepository;
import site.travellaboratory.be.review.domain.Review;
import site.travellaboratory.be.review.domain.ReviewLike;
import site.travellaboratory.be.review.domain.enums.ReviewLikeStatus;
import site.travellaboratory.be.review.domain.enums.ReviewStatus;
import site.travellaboratory.be.test.mock.review.FakeReviewLikeRepository;
import site.travellaboratory.be.test.mock.review.FakeReviewRepository;
import site.travellaboratory.be.test.mock.user.FakeUserRepository;
import site.travellaboratory.be.user.application.port.UserRepository;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain.enums.UserStatus;

class ReviewLikeServiceTest {

    private ReviewLikeService sut;
    private ReviewRepository reviewRepository;
    private ReviewLikeRepository reviewLikeRepository;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        this.reviewRepository = new FakeReviewRepository();
        this.reviewLikeRepository = new FakeReviewLikeRepository();
        this.userRepository = new FakeUserRepository();
        this.sut = new ReviewLikeService(reviewRepository, reviewLikeRepository, userRepository);
    }

    @Nested
    class ToggleLikeReview {

        private User user;
        private Review review;

        @BeforeEach
        void setUp() {
            this.user = userRepository.save(
                User.builder()
                    .username("userA@email.com")
                    .status(UserStatus.ACTIVE)
                    .isAgreement(true)
                    .build()
            );

            this.review = reviewRepository.save(
                Review.builder()
                    .id(1L)
                    .user(user)
                    .status(ReviewStatus.ACTIVE)
                    .build()
            );
        }

        @DisplayName("유효하지_않은_후기_ID일_경우_예외_반환")
        @Test
        void test1() {
            //given
            Long userId = user.getId();
            Long invalidReviewId = 9999L;

            //when
            BeApplicationException exception = assertThrows(
                BeApplicationException.class, () -> {
                    sut.toggleLike(userId, invalidReviewId);
                });

            //then
            assertEquals(ErrorCodes.REVIEW_LIKE_INVALID_REVIEW_ID, exception.getErrorCodes());
        }

        @DisplayName("성공 - 후기_좋아요")
        @Test
        void test1000() {
            //given
            Long userId = user.getId();
            Long reviewId = review.getId();

            //when
            ReviewLike result = sut.toggleLike(userId, reviewId);

            //then
            assertNotNull(result);
            assertEquals(ReviewLikeStatus.ACTIVE, result.getStatus());
        }

        @DisplayName("성공 - 후기_좋아요_토글로_취소")
        @Test
        void test1001() {
            //given
            Long userId = user.getId();
            Long reviewId = review.getId();

            // 첫 좋아요
            reviewLikeRepository.save(ReviewLike.builder()
                .id(1L)
                .user(user)
                .review(review)
                .status(ReviewLikeStatus.ACTIVE)
                .build());

            //when
            ReviewLike result = sut.toggleLike(userId, reviewId);

            //then
            assertNotNull(result);
            assertEquals(ReviewLikeStatus.INACTIVE, result.getStatus());
        }
    }
}