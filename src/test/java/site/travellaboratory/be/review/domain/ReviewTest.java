package site.travellaboratory.be.review.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import site.travellaboratory.be.article.domain.Article;
import site.travellaboratory.be.article.domain.enums.ArticleStatus;
import site.travellaboratory.be.common.presentation.exception.BeApplicationException;
import site.travellaboratory.be.common.presentation.error.ErrorCodes;
import site.travellaboratory.be.review.domain.enums.ReviewStatus;
import site.travellaboratory.be.review.domain.request.ReviewSaveRequest;
import site.travellaboratory.be.review.domain.request.ReviewUpdateRequest;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain.enums.UserStatus;

class ReviewTest {

    private User writer1;
    private Article article1;

    @BeforeEach
    void setUp() {
        // 유저
        writer1 = User.builder()
            .id(1L)
            .nickname("writer1")
            .status(UserStatus.ACTIVE)
            .build();

        // 유저가 쓴 여행계획
        article1 = Article.builder()
            .id(1L)
            .user(writer1)
            .status(ArticleStatus.ACTIVE)
            .build();
    }

    @Nested
    class createReview {
        @DisplayName("본인이_아닌_여행계획에_후기를_작성하려고_하는_경우_예외_반환")
        @Test
        void test1() {
            //given
            User otherUser = User.builder()
                .id(2L)
                .nickname("writer2")
                .status(UserStatus.ACTIVE)
                .build();

            ReviewSaveRequest saveRequest = ReviewSaveRequest.builder()
                .articleId(1L)
                .build();

            //when & then
            BeApplicationException exception = assertThrows(BeApplicationException.class, () ->
                Review.create(otherUser, article1, saveRequest));
            assertEquals(ErrorCodes.ARTICLE_VERIFY_OWNER, exception.getErrorCodes());
        }

        @DisplayName("성공 - ReviewSaveRequest_로_Review_객체_생성_(feat.본인의_여행계획에_대한_후기_작성)")
        @Test
        void test1000() {
            //given
            ReviewSaveRequest saveRequest = ReviewSaveRequest.builder()
                .articleId(1L)
                .title("review_title")
                .representativeImgUrl("review_Img_Url")
                .description("review_description")
                .build();

            //when
            Review review = Review.create(writer1, article1, saveRequest);

            //then
            assertNotNull(review);
            assertEquals(writer1, review.getUser());
            assertEquals(article1, review.getArticle());
            assertEquals(saveRequest.title(), review.getTitle());
            assertEquals(saveRequest.representativeImgUrl(), review.getRepresentativeImgUrl());
            assertEquals(saveRequest.description(), review.getDescription());
            assertEquals(ReviewStatus.ACTIVE, review.getStatus());
        }
    }

    @Nested
    class updateReview_withUpdatedContent {

        private Review review;

        @BeforeEach
        void setup() {
            review = Review.builder()
                .id(1L)
                .user(writer1)
                .article(article1)
                .title("title")
                .representativeImgUrl("img_url")
                .description("description")
                .status(ReviewStatus.ACTIVE) // ACTIVE -> INACTIVE
                .build();
        }

        @DisplayName("본인이_아닌_여행계획에_후기를_수정하려고_하는_경우_예외_반환")
        @Test
        void test1() {
            //given
            User otherUser = User.builder()
                .id(2L)
                .nickname("writer2")
                .status(UserStatus.ACTIVE)
                .build();

            ReviewUpdateRequest updateRequest = ReviewUpdateRequest.builder()
                .title("update_title")
                .representativeImgUrl("update_img_url")
                .description("update_description")
                .build();

            //when & then
            BeApplicationException exception = assertThrows(BeApplicationException.class, () ->
                review.withUpdatedContent(otherUser, updateRequest));
            assertEquals(ErrorCodes.REVIEW_VERIFY_OWNER, exception.getErrorCodes());
        }

        @DisplayName("성공 - ReviewUpdateRequest_로_새로운_Review_객체_생성_(feat.본인의_여행계획에_대한_후기_수정)")
        @Test
        void test1000() {
            //given
            ReviewUpdateRequest updateRequest = ReviewUpdateRequest.builder()
                .title("update_title")
                .representativeImgUrl("update_img_url")
                .description("update_description")
                .build();

            //when
            Review updateReview = review.withUpdatedContent(writer1, updateRequest);

            //then
            assertEquals(review.getId(), updateReview.getId());
            assertEquals(review.getUser(), updateReview.getUser());
            assertEquals(review.getArticle(), updateReview.getArticle());
            assertEquals(updateRequest.title(), updateReview.getTitle());
            assertEquals(updateRequest.representativeImgUrl(), updateReview.getRepresentativeImgUrl());
            assertEquals(updateRequest.description(), updateReview.getDescription());
            assertEquals(ReviewStatus.ACTIVE, updateReview.getStatus());
        }

    }

    @Nested
    class deleteReview_withInactiveStatus {
        private Review review;

        @BeforeEach
        void setup() {
            review = Review.builder()
                .id(1L)
                .user(writer1)
                .article(article1)
                .title("title")
                .representativeImgUrl("img_url")
                .description("description")
                .status(ReviewStatus.ACTIVE) // ACTIVE -> INACTIVE
                .build();
        }

        @DisplayName("본인이_아닌_여행계획에_후기를_삭제하려고_하는_경우_예외_반환")
        @Test
        void test1() {
            // given
            User otherUser = User.builder()
                .id(2L)
                .nickname("writer2")
                .status(UserStatus.ACTIVE)
                .build();

            // when & then
            BeApplicationException exception = assertThrows(BeApplicationException.class, () ->
                review.withInactiveStatus(otherUser));
            assertEquals(ErrorCodes.REVIEW_VERIFY_OWNER, exception.getErrorCodes());
        }

        @DisplayName("성공 - 본인의_여행계획에_대한_후기_삭제")
        @Test
        void test1000() {
            //when
            Review deleteReview = review.withInactiveStatus(writer1);

            //then
            assertEquals(ReviewStatus.INACTIVE, deleteReview.getStatus());
        }
    }
}