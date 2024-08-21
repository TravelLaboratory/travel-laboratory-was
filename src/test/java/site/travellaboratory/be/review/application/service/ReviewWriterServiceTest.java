package site.travellaboratory.be.review.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import site.travellaboratory.be.article.application.port.ArticleRepository;
import site.travellaboratory.be.article.domain.Article;
import site.travellaboratory.be.article.domain.enums.ArticleStatus;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.error.ErrorCodes;
import site.travellaboratory.be.review.application.port.ReviewRepository;
import site.travellaboratory.be.review.domain.Review;
import site.travellaboratory.be.review.domain.enums.ReviewStatus;
import site.travellaboratory.be.review.domain.request.ReviewSaveRequest;
import site.travellaboratory.be.review.domain.request.ReviewUpdateRequest;
import site.travellaboratory.be.test.mock.article.FakeArticleRepository;
import site.travellaboratory.be.test.mock.review.FakeReviewRepository;
import site.travellaboratory.be.test.mock.user.FakeUserRepository;
import site.travellaboratory.be.user.application.port.UserRepository;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain.enums.UserStatus;

class ReviewWriterServiceTest {

    private ReviewWriterService sut;
    private ReviewRepository reviewRepository;
    private UserRepository userRepository;
    private ArticleRepository articleRepository;

    @BeforeEach
    void setUp() {
        reviewRepository = new FakeReviewRepository();
        userRepository = new FakeUserRepository();
        articleRepository = new FakeArticleRepository();
        sut = new ReviewWriterService(reviewRepository, userRepository, articleRepository);
    }

    @Nested
    class Save {

        private User user;
        private Article article;

        @BeforeEach
        void setUp() {
            this.user = userRepository.save(
                User.builder()
                    .username("userA@email.com")
                    .status(UserStatus.ACTIVE)
                    .build()
            );

            this.article = articleRepository.save(
                Article.builder()
                    .user(user)
                    .status(ArticleStatus.ACTIVE)
                    .build()
            );
        }

        @DisplayName("유효하지_않은_userId일_경우_예외_반환")
        @Test
        void test1() {
            // given
            Long invalidUserId = 9999L;
            ReviewSaveRequest request = ReviewSaveRequest.builder()
                .articleId(article.getId())
                .title("title")
                .representativeImgUrl(null)
                .description("description")
                .build();

            // when
            BeApplicationException exception = assertThrows(BeApplicationException.class,
                () -> sut.save(invalidUserId, request));

            // then
            assertEquals(ErrorCodes.USER_NOT_FOUND, exception.getErrorCodes());
        }

        @DisplayName("유효하지_않은_articleId일_경우_예외_반환")
        @Test
        void test2() {
            // given
            Long invalidArticleId = 9999L;
            ReviewSaveRequest invalidRequest = ReviewSaveRequest.builder()
                .articleId(invalidArticleId)
                .build();

            // when
            BeApplicationException exception = assertThrows(BeApplicationException.class,
                () -> sut.save(user.getId(), invalidRequest));

            // then
            assertEquals(ErrorCodes.REVIEW_INVALID_ARTICLE_ID, exception.getErrorCodes());
        }

        @DisplayName("삭제된_여행_게획에_후기를_작성하려는_경우_예외_반환")
        @Test
        void test3() {
            // given
            Article inActiveArticle = articleRepository.save(
                Article.builder()
                    .user(user)
                    .status(ArticleStatus.INACTIVE)
                    .build()
            );

            ReviewSaveRequest invalidRequest = ReviewSaveRequest.builder()
                .articleId(inActiveArticle.getId())
                .build();

            // when
            BeApplicationException exception = assertThrows(BeApplicationException.class, () ->
                sut.save(user.getId(), invalidRequest));

            // then
            assertEquals(ErrorCodes.REVIEW_INVALID_ARTICLE_ID, exception.getErrorCodes());
        }

        @DisplayName("이미_후기가_존재하는_경우_예외_반환_(feat.여행계획에_대한_후기는_하나만_가능)")
        @Test
        void test4() {
            // given
            reviewRepository.save(Review.builder()
                .user(user)
                .article(article)
                .status(ReviewStatus.ACTIVE)
                .build());

            ReviewSaveRequest invalidRequest = ReviewSaveRequest.builder()
                .articleId(article.getId()) // 후기는 여행계획당 하나만 가능
                .build();

            // when
            BeApplicationException exception = assertThrows(BeApplicationException.class,
                () -> sut.save(user.getId(), invalidRequest));

            // then
            assertEquals(ErrorCodes.REVIEW_POST_EXIST, exception.getErrorCodes());
        }


        @DisplayName("성공 -  후기_저장")
        @Test
        void test1000() {
            // given
            ReviewSaveRequest request = ReviewSaveRequest.builder()
                .articleId(article.getId())
                .title("title")
                .representativeImgUrl("http://test.png")
                .description("description")
                .build();

            // when
            Review result = sut.save(user.getId(), request);

            // then
            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals(request.articleId(), result.getArticle().getId());
            assertEquals(request.title(), result.getTitle());
            assertEquals(request.representativeImgUrl(), result.getRepresentativeImgUrl());
            assertEquals(request.description(), result.getDescription());
            assertEquals(ReviewStatus.ACTIVE, result.getStatus());
        }
    }

    @Nested
    class Update {

        private User user;
        private Article article;

        private Review review;

        @BeforeEach
        void setUp() {
            this.user = userRepository.save(
                User.builder()
                    .username("userA@email.com")
                    .status(UserStatus.ACTIVE)
                    .build()
            );

            this.article = articleRepository.save(
                Article.builder()
                    .user(user)
                    .status(ArticleStatus.ACTIVE)
                    .build()
            );

            this.review = reviewRepository.save(
                Review.builder()
                    .user(user)
                    .article(article)
                    .status(ReviewStatus.ACTIVE)
                    .build()
            );
        }

        @DisplayName("유효하지_않은_userId일_경우_예외_반환")
        @Test
        void test1() {
            // given
            Long invalidUserId = 9999L;
            ReviewUpdateRequest request = ReviewUpdateRequest.builder()
                .title("title")
                .representativeImgUrl("http://test.png")
                .description("description")
                .build();

            // when
            BeApplicationException exception = assertThrows(BeApplicationException.class,
                () -> sut.update(invalidUserId, review.getId(), request));

            // then
            assertEquals(ErrorCodes.USER_NOT_FOUND, exception.getErrorCodes());
        }

        @DisplayName("탈퇴한_유저의_userId일_경우_예외_반환")
        @Test
        void test2() {
            // given
            User inActiveUser = userRepository.save(
                User.builder()
                    .username("userA@email.com")
                    .status(UserStatus.INACTIVE)
                    .build()
            );

            ReviewUpdateRequest request = ReviewUpdateRequest.builder()
                .title("title")
                .representativeImgUrl("http://test.png")
                .description("description")
                .build();

            // when
            BeApplicationException exception = assertThrows(BeApplicationException.class,
                () -> sut.update(inActiveUser.getId(), review.getId(), request));

            // then
            assertEquals(ErrorCodes.USER_NOT_FOUND, exception.getErrorCodes());
        }


        @DisplayName("삭제된_reviewId일_경우_예외_반환")
        @Test
        void test3() {
            // given
            Review deletedReview = reviewRepository.save(Review.builder()
                .user(user)
                .article(article)
                .status(ReviewStatus.INACTIVE).build());

            ReviewUpdateRequest request = ReviewUpdateRequest.builder()
                .title("title")
                .representativeImgUrl("http://test.png")
                .description("description")
                .build();

            // when
            BeApplicationException exception = assertThrows(BeApplicationException.class,
                () -> sut.update(user.getId(), deletedReview.getId(), request));

            // then
            assertEquals(ErrorCodes.REVIEW_INVALID_REVIEW_ID, exception.getErrorCodes());
        }


        @DisplayName("성공 - 후기_수정")
        @Test
        void test1000() {
            // given
            ReviewUpdateRequest request = ReviewUpdateRequest.builder()
                .title("update_title")
                .representativeImgUrl("update_representative_img_url")
                .description("update_description")
                .build();

            // when
            Review result = sut.update(user.getId(), review.getId(), request);

            // then
            assertNotNull(result);
            assertNotNull(result);
            assertEquals(review.getId(), result.getId());
            assertEquals(request.title(), result.getTitle());
            assertEquals(request.representativeImgUrl(), result.getRepresentativeImgUrl());
            assertEquals(request.description(), result.getDescription());
            assertEquals(ReviewStatus.ACTIVE, result.getStatus());
        }
    }

    @Nested
    class Delete {

        private User user;
        private Article article;

        private Review review;

        @BeforeEach
        void setUp() {
            this.user = userRepository.save(
                User.builder()
                    .username("userA@email.com")
                    .status(UserStatus.ACTIVE)
                    .build()
            );

            this.article = articleRepository.save(
                Article.builder()
                    .user(user)
                    .status(ArticleStatus.ACTIVE)
                    .build()
            );

            this.review = reviewRepository.save(
                Review.builder()
                    .user(user)
                    .article(article)
                    .status(ReviewStatus.ACTIVE)
                    .build()
            );
        }

        @DisplayName("유효하지_않은_reviewId일_경우_예외_반환")
        @Test
        void test1() {
            // given
            Long invalidReviewId = 9999L;

            // when
            BeApplicationException exception = assertThrows(BeApplicationException.class,
                () -> sut.delete(invalidReviewId, invalidReviewId));

            // then
            assertEquals(ErrorCodes.REVIEW_INVALID_REVIEW_ID, exception.getErrorCodes());
        }

        @DisplayName("삭제된_후기를_삭제하려고하는_경우_예외_반환")
        @Test
        void test2() {
            // given
            Review inActiveReview = reviewRepository.save(Review.builder()
                .user(user)
                .article(article)
                .status(ReviewStatus.INACTIVE)
                .build());

            // when
            BeApplicationException exception = assertThrows(BeApplicationException.class,
                () -> sut.delete(user.getId(), inActiveReview.getId()));

            // then
            assertEquals(ErrorCodes.REVIEW_INVALID_REVIEW_ID, exception.getErrorCodes());
        }


        // 도메인에서 이미 테스트하기에 중복 테스트
        @DisplayName("후기를_작성한_글쓴이가_아닌_다른_유저가_후기를_삭제하려고하는_경우_예외_반환")
        @Test
        void test3() {
            // given
            User anotherUser = userRepository.save(User.builder()
                .username("anotherUser")
                .status(UserStatus.ACTIVE)
                .build());

            // when
            BeApplicationException exception = assertThrows(BeApplicationException.class,
                () -> sut.delete(anotherUser.getId(), review.getId()));

            // then
            assertEquals(ErrorCodes.REVIEW_VERIFY_OWNER, exception.getErrorCodes());
        }

        @DisplayName("성공 - 후기_삭제")
        @Test
        void test1000() {
            //given (이미 setUp()메서드에 다 있음)

            // when
            Review result = sut.delete(user.getId(), review.getId());

            // then
            assertEquals(review.getId(), result.getId());
            assertEquals(ReviewStatus.INACTIVE, result.getStatus());
        }
    }
}
