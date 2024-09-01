package site.travellaboratory.be.comment.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import site.travellaboratory.be.comment.application.port.CommentRepository;
import site.travellaboratory.be.comment.domain.Comment;
import site.travellaboratory.be.comment.domain.enums.CommentStatus;
import site.travellaboratory.be.comment.domain.request.CommentSaveRequest;
import site.travellaboratory.be.comment.domain.request.CommentUpdateRequest;
import site.travellaboratory.be.common.presentation.exception.BeApplicationException;
import site.travellaboratory.be.common.presentation.error.ErrorCodes;
import site.travellaboratory.be.review.application.port.ReviewRepository;
import site.travellaboratory.be.review.domain.Review;
import site.travellaboratory.be.review.domain.enums.ReviewStatus;
import site.travellaboratory.be.test.mock.comment.FakeCommentRepository;
import site.travellaboratory.be.test.mock.review.FakeReviewRepository;
import site.travellaboratory.be.test.mock.user.FakeUserRepository;
import site.travellaboratory.be.user.application.port.UserRepository;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain.enums.UserStatus;

class CommentWriterServiceTest {

    private CommentWriterService sut;
    private CommentRepository commentRepository;
    private ReviewRepository reviewRepository;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        commentRepository = new FakeCommentRepository();
        reviewRepository = new FakeReviewRepository();
        userRepository = new FakeUserRepository();
        sut = new CommentWriterService(commentRepository, reviewRepository, userRepository);
    }

    @Nested
    class Save {

        private User user;
        private Review review;

        @BeforeEach
        void setUp() {
            this.user = userRepository.save(
                User.builder()
                    .username("userA@email.com")
                    .status(UserStatus.ACTIVE)
                    .build()
            );

            this.review = reviewRepository.save(
                Review.builder()
                    .user(user)
                    .status(ReviewStatus.ACTIVE)
                    .build()
            );
        }

        @DisplayName("유효하지_않은_userId일_경우_예외_반환")
        @Test
        void test1() {
            // given
            Long invalidUserId = 9999L;
            CommentSaveRequest request = CommentSaveRequest.builder()
                .reviewId(review.getId())
                .replyComment("replyComment()")
                .build();

            // when
            BeApplicationException exception = assertThrows(BeApplicationException.class,
                () -> sut.save(invalidUserId, request));

            // then
            assertEquals(ErrorCodes.USER_NOT_FOUND, exception.getErrorCodes());
        }

        @DisplayName("유효하지_않은_reviewId일_경우_예외_반환")
        @Test
        void test2() {
            // given
            Long invalidReviewId = 9999L;
            CommentSaveRequest invalidRequest = CommentSaveRequest.builder()
                .reviewId(invalidReviewId)
                .build();

            // when
            BeApplicationException exception = assertThrows(BeApplicationException.class,
                () -> sut.save(user.getId(), invalidRequest));

            // then
            assertEquals(ErrorCodes.COMMENT_INVALID_REVIEW_ID, exception.getErrorCodes());
        }

        @DisplayName("삭제된_리뷰에_댓글을_작성하려는_경우_예외_반환")
        @Test
        void test3() {
            // given
            Review inActiveReview = reviewRepository.save(
                Review.builder()
                    .user(user)
                    .status(ReviewStatus.INACTIVE)
                    .build()
            );

            CommentSaveRequest invalidRequest = CommentSaveRequest.builder()
                .reviewId(inActiveReview.getId())
                .build();

            // when
            BeApplicationException exception = assertThrows(BeApplicationException.class, () ->
                sut.save(user.getId(), invalidRequest));

            // then
            assertEquals(ErrorCodes.COMMENT_INVALID_REVIEW_ID, exception.getErrorCodes());
        }

        @DisplayName("성공 -  댓글_저장")
        @Test
        void test1000() {
            // given
            CommentSaveRequest request = CommentSaveRequest.builder()
                .reviewId(review.getId())
                .replyComment("reply_comment")
                .build();

            // when
            Comment result = sut.save(user.getId(), request);

            // then
            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals(request.reviewId(), result.getReview().getId());
            assertEquals(request.replyComment(), result.getReplyComment());
            assertEquals(CommentStatus.ACTIVE, result.getStatus());
        }
    }

    @Nested
    class Update {

        private User user;
        private Review review;
        private Comment comment;

        @BeforeEach
        void setUp() {
            this.user = userRepository.save(
                User.builder()
                    .username("userA@email.com")
                    .status(UserStatus.ACTIVE)
                    .build()
            );

            this.review = reviewRepository.save(
                Review.builder()
                    .user(user)
                    .status(ReviewStatus.ACTIVE)
                    .build()
            );

            this.comment = commentRepository.save(
                Comment.builder()
                    .user(user)
                    .review(review)
                    .status(CommentStatus.ACTIVE)
                    .build()
            );
        }

        @DisplayName("유효하지_않은_userId일_경우_예외_반환")
        @Test
        void test1() {
            // given
            Long invalidUserId = 9999L;
            CommentUpdateRequest request = CommentUpdateRequest.builder()
                .replyComment("reply_comment")
                .build();

            // when
            BeApplicationException exception = assertThrows(BeApplicationException.class,
                () -> sut.update(invalidUserId, comment.getId(), request));

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

            CommentUpdateRequest request = CommentUpdateRequest.builder()
                .replyComment("reply_comment")
                .build();

            // when
            BeApplicationException exception = assertThrows(BeApplicationException.class,
                () -> sut.update(inActiveUser.getId(), comment.getId(), request));

            // then
            assertEquals(ErrorCodes.USER_NOT_FOUND, exception.getErrorCodes());
        }

        @DisplayName("삭제된_commentId일_경우_예외_반환")
        @Test
        void test3() {
            // given
            Comment deletedComment = commentRepository.save(Comment.builder()
                .user(user)
                .review(review)
                .status(CommentStatus.INACTIVE)
                .build()
            );

            CommentUpdateRequest request = CommentUpdateRequest.builder()
                .replyComment("reply_comment")
                .build();

            // when
            BeApplicationException exception = assertThrows(BeApplicationException.class,
                () -> sut.update(user.getId(), deletedComment.getId(), request));

            // then
            assertEquals(ErrorCodes.COMMENT_INVALID_COMMENT_ID, exception.getErrorCodes());
        }

        @DisplayName("성공 - 댓글_수정")
        @Test
        void test1000() {
            // given
            CommentUpdateRequest request = CommentUpdateRequest.builder()
                .replyComment("update_reply_comment")
                .build();

            // when
            Comment result = sut.update(user.getId(), comment.getId(), request);

            // then
            assertNotNull(result);
            assertEquals(comment.getId(), result.getId());
            assertEquals(request.replyComment(), result.getReplyComment());
            assertEquals(CommentStatus.ACTIVE, result.getStatus());
        }
    }

    @Nested
    class Delete {

        private User user;
        private Review review;
        private Comment comment;

        @BeforeEach
        void setUp() {
            this.user = userRepository.save(
                User.builder()
                    .username("userA@email.com")
                    .status(UserStatus.ACTIVE)
                    .build()
            );

            this.review = reviewRepository.save(
                Review.builder()
                    .user(user)
                    .status(ReviewStatus.ACTIVE)
                    .build()
            );

            this.comment = commentRepository.save(
                Comment.builder()
                    .user(user)
                    .review(review)
                    .status(CommentStatus.ACTIVE)
                    .build()
            );
        }

        @DisplayName("유효하지_않은_commentId일_경우_예외_반환")
        @Test
        void test1() {
            // given
            Long invalidCommentId = 9999L;

            // when
            BeApplicationException exception = assertThrows(BeApplicationException.class,
                () -> sut.delete(invalidCommentId, invalidCommentId));

            // then
            assertEquals(ErrorCodes.COMMENT_INVALID_COMMENT_ID, exception.getErrorCodes());
        }

        @DisplayName("삭제된_댓글을_삭제하려고하는_경우_예외_반환")
        @Test
        void test2() {
            // given
            Comment inActiveComment = commentRepository.save(Comment.builder()
                .user(user)
                .review(review)
                .status(CommentStatus.INACTIVE)
                .build());

            // when
            BeApplicationException exception = assertThrows(BeApplicationException.class,
                () -> sut.delete(user.getId(), inActiveComment.getId()));

            // then
            assertEquals(ErrorCodes.COMMENT_INVALID_COMMENT_ID, exception.getErrorCodes());
        }

        // 도메인에서 이미 테스트하기에 중복 테스트
        @DisplayName("댓글을_작성한_글쓴이가_아닌_다른_유저가_댓글을_삭제하려고하는_경우_예외_반환")
        @Test
        void test3() {
            // given
            User anotherUser = userRepository.save(User.builder()
                .username("anotherUser")
                .status(UserStatus.ACTIVE)
                .build());

            // when
            BeApplicationException exception = assertThrows(BeApplicationException.class,
                () -> sut.delete(anotherUser.getId(), comment.getId()));

            // then
            assertEquals(ErrorCodes.COMMENT_VERIFY_OWNER, exception.getErrorCodes());
        }

        @DisplayName("성공 - 댓글_삭제")
        @Test
        void test1000() {
            //given (이미 setUp()메서드에 다 있음)

            // when
            Comment result = sut.delete(user.getId(), comment.getId());

            // then
            assertEquals(comment.getId(), result.getId());
            assertEquals(CommentStatus.INACTIVE, result.getStatus());
        }
    }
}
