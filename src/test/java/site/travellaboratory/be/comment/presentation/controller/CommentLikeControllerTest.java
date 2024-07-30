package site.travellaboratory.be.comment.presentation.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import site.travellaboratory.be.article.domain.Article;
import site.travellaboratory.be.article.domain.enums.ArticleStatus;
import site.travellaboratory.be.comment.application.service.CommentLikeService;
import site.travellaboratory.be.comment.domain.Comment;
import site.travellaboratory.be.comment.domain.CommentLike;
import site.travellaboratory.be.comment.domain.enums.CommentLikeStatus;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.common.presentation.config.JsonConfig;
import site.travellaboratory.be.common.presentation.resolver.AuthenticatedUserIdResolver;
import site.travellaboratory.be.review.domain.Review;
import site.travellaboratory.be.review.domain.enums.ReviewStatus;
import site.travellaboratory.be.test.assertion.Assertions;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain._auth.enums.UserRole;
import site.travellaboratory.be.user.domain.enums.UserStatus;
import site.travellaboratory.be.user.infrastructure.jwt.interceptor.AuthorizationInterceptor;

@Import({JsonConfig.class, AuthenticatedUserIdResolver.class})
@WebMvcTest(CommentLikeController.class)
class CommentLikeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CommentLikeService commentLikeService;

    @MockBean
    private AuthorizationInterceptor authorizationInterceptor;

    private User user;

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

        Article article = Article.builder()
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
    @DisplayName("[PUT] 댓글 좋아요 토글 /api/v1/comments/{commentId}/likes")
    class ToggleLike {
        @DisplayName("[실패] 유효하지 않은 댓글에 좋아요하려고 할 경우 - 404 Not Found 반환")
        @Test
        void test1() throws Exception {
            // given
            Long userId = user.getId();
            Long invalidCommentId = 9999L;

            given(commentLikeService.toggleLike(eq(userId), eq(invalidCommentId)))
                .willThrow(new BeApplicationException(ErrorCodes.COMMENT_LIKE_INVALID_COMMENT_ID, HttpStatus.NOT_FOUND));

            // when
            MvcResult result = mockMvc.perform(
                    put("/api/v1/comments/{commentId}/likes", invalidCommentId)
                        .contentType("application/json")
                        .header("authorization-token", "validTokenWithUserId"))
                .andExpect(status().isNotFound())
                .andReturn();
            
            //then
            Assertions.assertMvcErrorEquals(result, ErrorCodes.COMMENT_LIKE_INVALID_COMMENT_ID);
            verify(commentLikeService).toggleLike(userId, invalidCommentId);
        }

        @DisplayName("[성공] 댓글 좋아요 토글 - 200 OK 반환")
        @Test
        void test1000() throws Exception {
            // given
            Long userId = user.getId();
            Comment comment = Comment.builder()
                .id(1L)
                .user(user)
                .review(review)
                .replyComment("reply_comment")
                .build();

            CommentLike commentLike = CommentLike.builder()
                .user(user)
                .comment(comment)
                .status(CommentLikeStatus.ACTIVE)
                .build();

            given(commentLikeService.toggleLike(eq(userId), eq(comment.getId())))
                .willReturn(commentLike);

            // when
            MvcResult result = mockMvc.perform(
                    put("/api/v1/comments/{commentId}/likes", comment.getId())
                        .contentType("application/json")
                        .header("authorization-token", "validTokenWithUserId"))
                .andExpect(status().isOk())
                .andReturn();

            //then
            Assertions.assertMvcDataEquals(result, dataField -> {
                assertEquals(commentLike.getStatus().toString(), dataField.get("status").asText());
            });
            verify(commentLikeService).toggleLike(userId, comment.getId());
        }
    }
}