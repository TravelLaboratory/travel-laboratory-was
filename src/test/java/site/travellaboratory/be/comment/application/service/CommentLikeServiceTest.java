package site.travellaboratory.be.comment.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import site.travellaboratory.be.comment.application.port.CommentLikeRepository;
import site.travellaboratory.be.comment.application.port.CommentRepository;
import site.travellaboratory.be.comment.domain.Comment;
import site.travellaboratory.be.comment.domain.CommentLike;
import site.travellaboratory.be.comment.domain.enums.CommentLikeStatus;
import site.travellaboratory.be.comment.domain.enums.CommentStatus;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.test.mock.comment.FakeCommentLikeRepository;
import site.travellaboratory.be.test.mock.comment.FakeCommentRepository;
import site.travellaboratory.be.test.mock.user.FakeUserRepository;
import site.travellaboratory.be.user.application.port.UserRepository;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain.enums.UserStatus;

class CommentLikeServiceTest {

    private CommentLikeService sut;
    private CommentRepository commentRepository;
    private CommentLikeRepository commentLikeRepository;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        this.commentRepository = new FakeCommentRepository();
        this.commentLikeRepository = new FakeCommentLikeRepository();
        this.userRepository = new FakeUserRepository();
        this.sut = new CommentLikeService(commentRepository, commentLikeRepository, userRepository);
    }

    @Nested
    class toggleLike {

        private User user;
        private Comment comment;

        @BeforeEach
        void setUp() {
            this.user = userRepository.save(
                User.builder()
                    .username("userA@email.com")
                    .status(UserStatus.ACTIVE)
                    .build()
            );

            this.comment = commentRepository.save(
                Comment.builder()
                    .id(1L)
                    .user(user)
                    .status(CommentStatus.ACTIVE)
                    .build()
            );
        }

        @DisplayName("유효하지_않은_댓글_ID일_경우_예외_반환")
        @Test
        void tes1() {
            // given
            Long invalidCommentId = 9999L;

            // when
            BeApplicationException exception = assertThrows(BeApplicationException.class,
                () -> sut.toggleLike(user.getId(), invalidCommentId));

            // then
            assertEquals(ErrorCodes.COMMENT_LIKE_INVALID_COMMENT_ID, exception.getErrorCodes());
        }

        @DisplayName("성공 - 댓글_좋아요")
        @Test
        void test1000() {
            //given
            Long commentId = comment.getId();

            //when
            CommentLike result = sut.toggleLike(user.getId(), commentId);

            //then
            assertNotNull(result);
            assertEquals(CommentLikeStatus.ACTIVE, result.getStatus());
        }

        @DisplayName("성공 - 댓글_좋아요_토글로_취소")
        @Test
        void test1001() {
            //given
            // 첫 좋아요
            CommentLike likedComment = commentLikeRepository.save(CommentLike.builder()
                .user(user)
                .comment(comment)
                .status(CommentLikeStatus.ACTIVE)
                .build());

            // when
            CommentLike result = sut.toggleLike(user.getId(), likedComment.getId());// 좋아요 취소

            // then
            assertNotNull(result);
            assertEquals(CommentLikeStatus.INACTIVE, result.getStatus());
        }
    }
}