package site.travellaboratory.be.comment.presentation.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static site.travellaboratory.be.test.assertion.Assertions.assertMvcDataEquals;
import static site.travellaboratory.be.test.assertion.Assertions.assertMvcErrorEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import site.travellaboratory.be.article.domain.Article;
import site.travellaboratory.be.article.domain.enums.ArticleStatus;
import site.travellaboratory.be.comment.application.service.CommentWriterService;
import site.travellaboratory.be.comment.domain.Comment;
import site.travellaboratory.be.comment.domain.enums.CommentStatus;
import site.travellaboratory.be.comment.domain.request.CommentSaveRequest;
import site.travellaboratory.be.comment.domain.request.CommentUpdateRequest;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.error.ErrorCodes;
import site.travellaboratory.be.common.presentation.config.JsonConfig;
import site.travellaboratory.be.common.presentation.resolver.AuthenticatedUserIdResolver;
import site.travellaboratory.be.review.domain.Review;
import site.travellaboratory.be.review.domain.enums.ReviewStatus;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain._auth.enums.UserRole;
import site.travellaboratory.be.user.domain.enums.UserStatus;
import site.travellaboratory.be.user.infrastructure.jwt.interceptor.AuthorizationInterceptor;

@Import({JsonConfig.class, AuthenticatedUserIdResolver.class})
@WebMvcTest({CommentWriterController.class})
class CommentWriterControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CommentWriterService commentWriterService;

    @MockBean
    private AuthorizationInterceptor authorizationInterceptor;

    private User user;
    private Article article;
    private Review review;

    @BeforeEach
    void setUp() throws Exception {
        // User와 Article 객체 생성
        this.user = User.builder()
            .id(1L)
            .username("user1@gmail.com")
            .password("password")
            .role(UserRole.USER)
            .profileImgUrl(null)
            .introduce(null)
            .status(UserStatus.ACTIVE)
            .isAgreement(true)
            .build();

        this.article = Article.builder()
            .id(1L)
            .user(user)
            .title("article_title")
            .status(ArticleStatus.ACTIVE)
            .build();

        this.review = Review.builder()
            .id(1L)
            .article(article)
            .user(user)
            .title("title")
            .description("description")
            .status(ReviewStatus.ACTIVE)
            .build();

        // 인터셉터 모킹 설정 - 토큰 검증 및 userId 설정
        given(authorizationInterceptor.preHandle(any(HttpServletRequest.class),
            any(HttpServletResponse.class), any(Object.class)))
            .willAnswer(invocation -> {
                HttpServletRequest request = invocation.getArgument(0);
                String accessToken = request.getHeader("authorization-token");
                if (accessToken == null) {
                    throw new BeApplicationException(ErrorCodes.TOKEN_AUTHORIZATION_TOKEN_NOT_FOUND,
                        HttpStatus.BAD_REQUEST);
                }
                Long userId = user.getId(); // Mock user ID
                RequestAttributes requestAttributes = Objects.requireNonNull(
                    RequestContextHolder.getRequestAttributes());
                requestAttributes.setAttribute("userId", userId, RequestAttributes.SCOPE_REQUEST);
                return true;
            });
    }

    @Nested
    @DisplayName("[POST] 댓글 작성 /api/v1/comment")
    class Save {

        @DisplayName("[실패] 유효하지 않은 후기 ID인 경우 - 404 Not Found 반환")
        @Test
        void test1() throws Exception {
            //given
            Long invalidReviewId = 9999L;

            CommentSaveRequest request = CommentSaveRequest.builder()
                .reviewId(invalidReviewId)
                .replyComment("reply_comment")
                .build();

            given(commentWriterService.save(eq(user.getId()),
                any(CommentSaveRequest.class))).willThrow(
                new BeApplicationException(ErrorCodes.REVIEW_INVALID_REVIEW_ID,
                    HttpStatus.NOT_FOUND)
            );

            //when
            MvcResult result = mockMvc.perform(post("/api/v1/comment")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(request))
                    .header("authorization-token", "validToken")
                )
                .andExpect(status().isNotFound())
                .andReturn();

            //then
            assertMvcErrorEquals(result, ErrorCodes.REVIEW_INVALID_REVIEW_ID);
            verify(commentWriterService).save(user.getId(), request);
        }

        @DisplayName("[성공] 댓글 작성 - 201 Created 반환")
        @Test
        void test1000() throws Exception {
            //given
            Long reviewId = review.getId();

            CommentSaveRequest request = CommentSaveRequest.builder()
                .reviewId(reviewId)
                .replyComment("reply_comment")
                .build();

            Comment comment = Comment.builder()
                .id(reviewId)
                .user(user)
                .review(review)
                .replyComment("very Good~~~")
                .status(CommentStatus.ACTIVE)
                .build();

            given(commentWriterService.save(eq(reviewId), any(CommentSaveRequest.class)))
                .willReturn(comment);

            //when
            MvcResult result = mockMvc.perform(post("/api/v1/comment")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(request))
                    .header("authorization-token", "validToken")
                )
                .andExpect(status().isCreated())
                .andReturn();

            //then
            assertMvcDataEquals(result, dataField -> {
                assertEquals(comment.getId(), dataField.get("comment_id").asLong());
            });
            verify(commentWriterService).save(user.getId(), request);
        }
    }

    @Nested
    @DisplayName("[PATCH] 댓글 작성 /api/v1/comments/{commentId}")
    class Update {

        @DisplayName("[실패] 유효하지 않은 댓글 ID - 404 Not Found 반환")
        @Test
        void test1() throws Exception {
            //given
            Long invalidCommentId = 9999L;

            CommentUpdateRequest request = CommentUpdateRequest.builder()
                .replyComment("Updated comment!")
                .build();

            given(commentWriterService.update(eq(user.getId()), eq(invalidCommentId),
                any(CommentUpdateRequest.class))).willThrow(
                new BeApplicationException(ErrorCodes.COMMENT_INVALID_COMMENT_ID,
                    HttpStatus.NOT_FOUND)
            );

            //when & then
            MvcResult result = mockMvc.perform(
                    patch("/api/v1/comments/{commentId}", invalidCommentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                        .header("authorization-token", "validTokenWithUserId"))
                .andExpect(status().isNotFound())
                .andReturn();

            //then
            assertMvcErrorEquals(result, ErrorCodes.COMMENT_INVALID_COMMENT_ID);
            verify(commentWriterService).update(user.getId(), invalidCommentId, request);
        }

        @DisplayName("[실패] - 유저가 작성한 댓글이 아닌 경우 - 403 Forbidden 반환")
        @Test
        void test2() throws Exception {
            //given
            Long commentIdNotOwnedByUser = 2L;

            CommentUpdateRequest request = CommentUpdateRequest.builder()
                .replyComment("Updated comment!")
                .build();

            given(commentWriterService.update(eq(user.getId()), eq(commentIdNotOwnedByUser),
                any(CommentUpdateRequest.class))).willThrow(
                new BeApplicationException(ErrorCodes.COMMENT_VERIFY_OWNER, HttpStatus.FORBIDDEN)
            );

            //when
            MvcResult result = mockMvc.perform(
                    patch("/api/v1/comments/{commentId}", commentIdNotOwnedByUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                        .header("authorization-token", "validTokenWithUserId"))
                .andExpect(status().isForbidden())
                .andReturn();

            //then
            assertMvcErrorEquals(result, ErrorCodes.COMMENT_VERIFY_OWNER);
            verify(commentWriterService).update(user.getId(), commentIdNotOwnedByUser, request);
        }

        @DisplayName("[성공] 댓글 수정 - 200 OK 반환")
        @Test
        void test1000() throws Exception {
            //given
            Long commentId = 1L;

            CommentUpdateRequest request = CommentUpdateRequest.builder()
                .replyComment("Updated comment!")
                .build();

            Comment updatedComment = Comment.builder()
                .id(commentId)
                .user(user)
                .review(review)
                .replyComment("Updated comment!")
                .status(CommentStatus.ACTIVE)
                .build();

            given(commentWriterService.update(eq(user.getId()), eq(commentId),
                any(CommentUpdateRequest.class))).willReturn(updatedComment);

            //when
            MvcResult result = mockMvc.perform(patch("/api/v1/comments/{commentId}", commentId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(request))
                    .header("authorization-token", "validTokenWithUserId"))
                .andExpect(status().isOk())
                .andReturn();

            //then
            assertMvcDataEquals(result, dataField -> {
                assertEquals(updatedComment.getId(), dataField.get("comment_id").asLong());
            });
            verify(commentWriterService).update(user.getId(), commentId, request);
        }
    }

    @Nested
    @DisplayName("[PATCH] 댓글 삭제 /api/v1/comments/{commentId}/status")
    class Delete {
        @DisplayName("[실패] 유효하지 않은 댓글 ID - 404 Not Found 반환")
        @Test
        void test1() throws Exception {
            //given
            Long invalidCommentId = 9999L;

            given(commentWriterService.delete(eq(user.getId()), eq(invalidCommentId)))
                .willThrow(new BeApplicationException(ErrorCodes.COMMENT_INVALID_COMMENT_ID,
                    HttpStatus.NOT_FOUND));

            //when
            MvcResult result = mockMvc.perform(
                    patch("/api/v1/comments/{commentId}/status", invalidCommentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization-token", "validTokenWithUserId"))
                .andExpect(status().isNotFound())
                .andReturn();

            //then
            assertMvcErrorEquals(result, ErrorCodes.COMMENT_INVALID_COMMENT_ID);
            verify(commentWriterService).delete(user.getId(), invalidCommentId);
        }

        @DisplayName("[실패] 유저가 작성한 댓글이 아닌 경우 - 403 Forbidden 반환")
        @Test
        void test2() throws Exception {
            //given
            Long commentIdNotOwnedByUser = 2L;

            given(commentWriterService.delete(eq(user.getId()), eq(commentIdNotOwnedByUser)))
                .willThrow(new BeApplicationException(ErrorCodes.COMMENT_VERIFY_OWNER,
                    HttpStatus.FORBIDDEN));

            //when
            MvcResult result = mockMvc.perform(
                    patch("/api/v1/comments/{commentId}/status", commentIdNotOwnedByUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization-token", "validTokenWithUserId"))
                .andExpect(status().isForbidden())
                .andReturn();

            //then
            assertMvcErrorEquals(result, ErrorCodes.COMMENT_VERIFY_OWNER);
            verify(commentWriterService).delete(user.getId(), commentIdNotOwnedByUser);
        }

        @DisplayName("[성공] 댓글 삭제 - 200 OK 반환")
        @Test
        void test1000() throws Exception {
            //given
            Long commentId = 1L;

            Comment deletedComment = Comment.builder()
                .id(commentId)
                .user(user)
                .review(review)
                .replyComment("This comment is deleted.")
                .status(CommentStatus.INACTIVE)
                .build();

            given(commentWriterService.delete(eq(user.getId()), eq(commentId)))
                .willReturn(deletedComment);

            //when
            MvcResult result = mockMvc.perform(
                    patch("/api/v1/comments/{commentId}/status", commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization-token", "validTokenWithUserId"))
                .andExpect(status().isOk())
                .andReturn();

            //then
            assertMvcDataEquals(result, dataField -> {
                assertTrue(dataField.get("is_delete").asBoolean());
            });
            verify(commentWriterService).delete(user.getId(), commentId);
        }
    }
}