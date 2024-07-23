package site.travellaboratory.be.comment.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import site.travellaboratory.be.article.domain.Article;
import site.travellaboratory.be.article.domain.enums.ArticleStatus;
import site.travellaboratory.be.comment.domain.enums.CommentStatus;
import site.travellaboratory.be.comment.domain.request.CommentSaveRequest;
import site.travellaboratory.be.comment.domain.request.CommentUpdateRequest;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.review.domain.Review;
import site.travellaboratory.be.review.domain.enums.ReviewStatus;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain.enums.UserStatus;

class CommentTest {

    private User writer1;
    private Review review1;

    @BeforeEach
    void setUp() {
        // 유저
        writer1 = User.builder()
            .id(1L)
            .nickname("writer1")
            .status(UserStatus.ACTIVE)
            .build();

        // 유저가 쓴 여행계획
        Article article1 = Article.builder()
            .id(1L)
            .user(writer1)
            .status(ArticleStatus.ACTIVE)
            .build();

        // 여행 계획에 대한 후기(한 개만 가능)
        review1 = Review.builder()
            .id(article1.getId())
            .user(writer1)
            .article(article1)
            .status(ReviewStatus.ACTIVE)
            .build();
    }

    @Nested
    class CreateComment {
        @DisplayName("성공 - CommentSaveReqeust_로_Comment_객체_생성")
        @Test
        void test1000() {
            //given
            CommentSaveRequest saveRequest = CommentSaveRequest.builder()
                .reviewId(review1.getId())
                .replyComment("comment1")
                .build();

            //when
            Comment comment = Comment.create(writer1, review1, saveRequest);

            //then
            assertNotNull(comment);
            assertEquals(saveRequest.reviewId(), comment.getReview().getId());
            assertEquals(saveRequest.replyComment(), comment.getReplyComment());
        }
    }

    @Nested
    class update_withUpdatedReplyContent {

        private Comment comment1;

        @BeforeEach
        void setUp() {
            comment1 = Comment.builder()
                .id(1L)
                .user(writer1)
                .review(review1)
                .replyComment("review1_comment")
                .status(CommentStatus.ACTIVE)
                .build();
        }

        @DisplayName("본인이_작성하지_않은_댓글을_수정하려고_하는_경우_예외_반환")
        @Test
        void test1() {
            //given
            User otherUser = User.builder()
                .id(2L)
                .nickname("otherUser")
                .status(UserStatus.ACTIVE)
                .build();

            CommentUpdateRequest updateRequest = CommentUpdateRequest.builder()
                .replyComment("update_comment")
                .build();

            //when & then
            BeApplicationException exception = assertThrows(BeApplicationException.class, () ->
                comment1.withUpdatedReplyContent(otherUser, updateRequest));
            assertEquals(ErrorCodes.COMMENT_VERIFY_OWNER, exception.getErrorCodes());
        }

        @DisplayName("성공 - 본인이_작성한_댓글을_수정")
        @Test
        void test1000() {
            //given
            CommentUpdateRequest updateRequest = CommentUpdateRequest.builder()
                .replyComment("update_comment")
                .build();

            //when
            Comment updateComment = comment1.withUpdatedReplyContent(writer1, updateRequest);

            //then
            assertEquals(comment1.getId(), updateComment.getId());
            assertEquals(comment1.getUser(), updateComment.getUser());
            assertEquals(comment1.getReview(), updateComment.getReview());
            assertEquals(updateRequest.replyComment(), updateComment.getReplyComment());
            assertEquals(comment1.getStatus(), updateComment.getStatus());
        }
    }


    @Nested
    class delete_withInactiveStatus {

        private Comment comment1;

        @BeforeEach
        void setUp() {
            comment1 = Comment.builder()
                .id(1L)
                .user(writer1)
                .review(review1)
                .replyComment("review1_comment")
                .status(CommentStatus.ACTIVE)
                .build();
        }

        @DisplayName("본인이_작성하지_않은_댓글을_삭제하려고_하는_경우_예외_반환")
        @Test
        void test1() {
            //given
            User otherUser = User.builder()
                .id(2L)
                .nickname("otherUser")
                .status(UserStatus.ACTIVE)
                .build();

            //when & then
            BeApplicationException exception = assertThrows(BeApplicationException.class, () ->
                comment1.withInactiveStatus(otherUser));
            assertEquals(ErrorCodes.COMMENT_VERIFY_OWNER, exception.getErrorCodes());
        }

        @DisplayName("성공 - 본인이_작성한_댓글을_삭제")
        @Test
        void test1000() {
            //when
            Comment deleteComment = comment1.withInactiveStatus(writer1);

            //then
            assertEquals(CommentStatus.INACTIVE, deleteComment.getStatus());
        }
    }
}