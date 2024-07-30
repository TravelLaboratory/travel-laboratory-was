package site.travellaboratory.be.review.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import site.travellaboratory.be.review.domain.enums.ReviewLikeStatus;
import site.travellaboratory.be.review.domain.enums.ReviewStatus;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain.enums.UserStatus;

class ReviewLikeTest {

    private User user1;
    private Review review1;

    @BeforeEach
    void setUp() {
        // 유저 생성
        user1 = User.builder()
            .id(1L)
            .nickname("user1")
            .status(UserStatus.ACTIVE)
            .isAgreement(true)
            .build();

        // 리뷰 생성
        review1 = Review.builder()
            .id(1L)
            .user(user1)
            .status(ReviewStatus.ACTIVE)
            .build();
    }

    @Nested
    class Create {

        @DisplayName("성공 - ReviewLike_객체_생성")
        @Test
        void test1000() {
            //given
            User user = user1;
            Review review = review1;

            //when
            ReviewLike reviewLike = ReviewLike.create(user, review);

            //then
            assertNotNull(reviewLike);
            assertEquals(user, reviewLike.getUser());
            assertEquals(review, reviewLike.getReview());
            assertEquals(ReviewLikeStatus.ACTIVE, reviewLike.getStatus());
            assertNotNull(reviewLike.getCreatedAt());
            assertNotNull(reviewLike.getUpdatedAt());
        }
    }

    @Nested
    class ToggleStatus {
        @DisplayName("성공 - 좋아요_상태를_ACTIVE에서_INACTIVE로_토글")
        @Test
        void test1000() {
            //given
            ReviewLike reviewLike = ReviewLike.builder()
                .id(1L)
                .user(user1)
                .review(review1)
                .status(ReviewLikeStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

            //when
            ReviewLike result = reviewLike.withToggleStatus();

            //then
            assertNotNull(result);
            assertEquals(reviewLike.getUser(), result.getUser());
            assertEquals(reviewLike.getReview(), result.getReview());
            assertEquals(ReviewLikeStatus.INACTIVE, result.getStatus());
            assertEquals(reviewLike.getCreatedAt(), result.getCreatedAt());
            assertNotNull(result.getUpdatedAt());
        }

        @DisplayName("성공 - 좋아요_상태를_INACTIVE에서_ACTIVE로_토글")
        @Test
        void test1001() {
            //given
            ReviewLike reviewLike = ReviewLike.builder()
                .id(1L)
                .user(user1)
                .review(review1)
                .status(ReviewLikeStatus.INACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

            //when
            ReviewLike result = reviewLike.withToggleStatus();

            //then
            assertNotNull(result);
            assertEquals(reviewLike.getUser(), result.getUser());
            assertEquals(reviewLike.getReview(), result.getReview());
            assertEquals(ReviewLikeStatus.ACTIVE, result.getStatus());
            assertEquals(reviewLike.getCreatedAt(), result.getCreatedAt());
            assertNotNull(result.getUpdatedAt());
        }
    }
}
