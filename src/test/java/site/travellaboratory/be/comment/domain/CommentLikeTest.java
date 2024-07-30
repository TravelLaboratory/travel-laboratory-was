package site.travellaboratory.be.comment.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import site.travellaboratory.be.comment.domain.enums.CommentLikeStatus;
import site.travellaboratory.be.comment.domain.enums.CommentStatus;
import site.travellaboratory.be.review.domain.Review;
import site.travellaboratory.be.review.domain.enums.ReviewStatus;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain.enums.UserStatus;

class CommentLikeTest {

    private User user1;
    private Comment comment1;

    @BeforeEach
    void setUp() {
        this.user1 = User.builder()
            .id(1L)
            .isAgreement(true)
            .status(UserStatus.ACTIVE)
            .build();

        Review review = Review.builder()
            .id(1L)
            .user(user1)
            .status(ReviewStatus.ACTIVE)
            .build();

        this.comment1 = Comment.builder()
            .id(1L)
            .user(user1)
            .review(review)
            .replyComment("reply_comment")
            .status(CommentStatus.ACTIVE)
            .build();
    }

    @Nested
    class Create {
        @DisplayName("성공 - CommentLike_객체_생성")
        @Test
        void test1000() {
            //given
            User user = user1;
            Comment comment = comment1;

            //when
            CommentLike commentLike = CommentLike.create(user, comment);

            //then
            assertNotNull(commentLike);
            assertEquals(user, commentLike.getUser());
            assertEquals(comment, commentLike.getComment());
            assertEquals(CommentLikeStatus.ACTIVE, commentLike.getStatus());
            assertNotNull(commentLike.getCreatedAt());
            assertNotNull(commentLike.getUpdatedAt());
        }

    }

    @Nested
    class WithToggleStatus {

        @DisplayName("성공 - 좋아요 상태를 ACTIVE에서 INACTIVE로 토글")
        @Test
        void test1000() {
            //given
            CommentLike commentLike = CommentLike.builder()
                .id(1L)
                .user(user1)
                .comment(comment1)
                .status(CommentLikeStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

            //when
            CommentLike result = commentLike.withToggleStatus();

            //then
            assertNotNull(commentLike);
            assertEquals(commentLike.getUser(),result.getUser());
            assertEquals(commentLike.getComment(),result.getComment());
            assertEquals(CommentLikeStatus.INACTIVE, result.getStatus());
            assertEquals(commentLike.getCreatedAt(), result.getCreatedAt());
            assertNotNull(commentLike.getUpdatedAt());
        }

    }
}