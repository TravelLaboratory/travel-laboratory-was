package site.travellaboratory.be.review.presentation.controller;

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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import site.travellaboratory.be.article.domain.Article;
import site.travellaboratory.be.article.domain.enums.ArticleStatus;
import site.travellaboratory.be.common.presentation.exception.BeApplicationException;
import site.travellaboratory.be.common.presentation.error.ErrorCodes;
import site.travellaboratory.be.common.presentation.config.JsonConfig;
import site.travellaboratory.be.common.presentation.resolver.AuthenticatedUserIdResolver;
import site.travellaboratory.be.review.application.service.ReviewLikeService;
import site.travellaboratory.be.review.domain.Review;
import site.travellaboratory.be.review.domain.ReviewLike;
import site.travellaboratory.be.review.domain.enums.ReviewLikeStatus;
import site.travellaboratory.be.review.domain.enums.ReviewStatus;
import site.travellaboratory.be.test.assertion.Assertions;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain._auth.enums.UserRole;
import site.travellaboratory.be.user.domain.enums.UserStatus;
import site.travellaboratory.be.user.infrastructure.jwt.interceptor.AuthorizationInterceptor;

@Import({JsonConfig.class, AuthenticatedUserIdResolver.class})
@WebMvcTest(ReviewLikeController.class)
class ReviewLikeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ReviewLikeService reviewLikeService;

    @MockBean
    private AuthorizationInterceptor authorizationInterceptor;

    private User user;

    private Article article;
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
    @DisplayName("[PUT] 후기 좋아요 토글 /api/v1/reviews/{reviewId}/likes")
    class ToggleLike {

        @DisplayName("[실패] 유효하지 않은 후기 ID일 경우 - 404 Not Found 반환")
        @Test
        void test1() throws Exception {
            //given
            Long userId = user.getId();
            Long invalidReviewId = 9999L;

            given(reviewLikeService.toggleLike(eq(userId), eq(invalidReviewId)))
                .willThrow(new BeApplicationException(ErrorCodes.REVIEW_LIKE_INVALID_REVIEW_ID, HttpStatus.NOT_FOUND));

            // when
            MvcResult result = mockMvc.perform(
                    put("/api/v1/reviews/{reviewId}/likes", invalidReviewId)
                        .contentType("application/json")
                        .header("authorization-token", "validTokenWithUserId"))
                .andExpect(status().isNotFound())
                .andReturn();

            //then
            Assertions.assertMvcErrorEquals(result, ErrorCodes.REVIEW_LIKE_INVALID_REVIEW_ID);
            verify(reviewLikeService).toggleLike(userId, invalidReviewId);
        }

        @DisplayName("[성공] 후기 좋아요 - 200 Ok 반환")
        @Test
        void testLikeReview() throws Exception {
            //given
            Long userId = user.getId();
            Review review = Review.builder()
                .id(1L)
                .article(article)
                .user(user)
                .title("title")
                .description("description")
                .status(ReviewStatus.ACTIVE)
                .build();

            ReviewLike reviewLike = ReviewLike.builder()
                .user(user)
                .review(review)
                .status(ReviewLikeStatus.ACTIVE)
                .build();

            given(reviewLikeService.toggleLike(eq(userId), eq(review.getId())))
                .willReturn(reviewLike);

            //when
            MvcResult result = mockMvc.perform(
                    put("/api/v1/reviews/{reviewId}/likes", review.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization-token", "validTokenWithUserId"))
                .andExpect(status().isOk())
                .andReturn();

            //then
            Assertions.assertMvcDataEquals(result, dataField -> {
                assertEquals(reviewLike.getStatus().toString(), dataField.get("status").asText());
            });
            verify(reviewLikeService).toggleLike(userId, review.getId());
        }

    }
}