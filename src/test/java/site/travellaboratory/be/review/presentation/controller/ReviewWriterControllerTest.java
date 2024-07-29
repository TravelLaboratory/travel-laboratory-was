package site.travellaboratory.be.review.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import site.travellaboratory.be.article.domain.Article;
import site.travellaboratory.be.article.domain.enums.ArticleStatus;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.common.presentation.config.JsonConfig;
import site.travellaboratory.be.common.presentation.resolver.AuthenticatedUserIdResolver;
import site.travellaboratory.be.review.application.service.ReviewWriterService;
import site.travellaboratory.be.review.domain.Review;
import site.travellaboratory.be.review.domain.enums.ReviewStatus;
import site.travellaboratory.be.review.domain.request.ReviewSaveRequest;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain._auth.enums.UserRole;
import site.travellaboratory.be.user.domain.enums.UserStatus;
import site.travellaboratory.be.user.infrastructure.jwt.interceptor.AuthorizationInterceptor;

@Import({JsonConfig.class, AuthenticatedUserIdResolver.class})
@WebMvcTest(ReviewWriterController.class)
class ReviewWriterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReviewWriterService reviewWriterService;

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
                Long userId = 1L; // Mock user ID
                RequestAttributes requestAttributes = Objects.requireNonNull(
                    RequestContextHolder.getRequestAttributes());
                requestAttributes.setAttribute("userId", userId, RequestAttributes.SCOPE_REQUEST);
                return true;
            });
    }

    @Nested
    @DisplayName("[POST] 후기 작성 /api/v1/review")
    class Save {

        @DisplayName("[실패] - 유효하지 않은 여행 계획 ID(ArticleId) - 404 Not Found 반환")
        @Test
        void test1() throws Exception {
            //given
            Long invalidArticleId = 9999L;

            ReviewSaveRequest request = ReviewSaveRequest.builder()
                .articleId(invalidArticleId)
                .title("review_title")
                .representativeImgUrl("https://review_img.png")
                .description("review_description")
                .status(ReviewStatus.ACTIVE)
                .build();

            given(reviewWriterService.save(eq(1L), any(ReviewSaveRequest.class))).willThrow(
                new BeApplicationException(ErrorCodes.REVIEW_INVALID_ARTICLE_ID,
                    HttpStatus.NOT_FOUND)
            );

            //when & then
            mockMvc.perform(post("/api/v1/review")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(request))
                    .header("authorization-token", "validTokenWithUserId"))
                .andExpect(status().isNotFound())
                .andExpect(
                    jsonPath("$.local_message").value(ErrorCodes.REVIEW_INVALID_ARTICLE_ID.message))
                .andExpect(jsonPath("$.code").value(ErrorCodes.REVIEW_INVALID_ARTICLE_ID.code))
            ;
        }

        @DisplayName("[실패] - 유저가 작성한 여행 계획이 아닌 경우 - 403 Forbidden 반환")
        @Test
        void test2() throws Exception {
            //given
            Long notOwnerArticleId = 2L;

            ReviewSaveRequest request = ReviewSaveRequest.builder()
                .articleId(notOwnerArticleId)
                .title("review_title")
                .representativeImgUrl("https://review_img.png")
                .description("review_description")
                .status(ReviewStatus.ACTIVE)
                .build();

            given(reviewWriterService.save(eq(1L), any(ReviewSaveRequest.class))).willThrow(
                new BeApplicationException(ErrorCodes.ARTICLE_VERIFY_OWNER, HttpStatus.FORBIDDEN)
            );

            //when & then
            mockMvc.perform(post("/api/v1/review")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(request))
                    .header("authorization-token", "validTokenWithUserId"))
                .andExpect(status().isForbidden())
                .andExpect(
                    jsonPath("$.local_message").value(ErrorCodes.ARTICLE_VERIFY_OWNER.message))
                .andExpect(jsonPath("$.code").value(ErrorCodes.ARTICLE_VERIFY_OWNER.code))

            ;
        }

        @DisplayName("[실패] - 이미 해당 여행 계획에 대한 후기가 있는 경우 - 409 Conflict 반환")
        @Test
        void test3() throws Exception {
            //given
            Long existReviewArticleId = 2L;

            ReviewSaveRequest request = ReviewSaveRequest.builder()
                .articleId(existReviewArticleId)
                .title("review_title")
                .representativeImgUrl("https://review_img.png")
                .description("review_description")
                .status(ReviewStatus.ACTIVE)
                .build();

            given(reviewWriterService.save(eq(1L), any(ReviewSaveRequest.class))).willThrow(
                new BeApplicationException(ErrorCodes.REVIEW_POST_EXIST, HttpStatus.CONFLICT)
            );

            //when & then
            mockMvc.perform(post("/api/v1/review")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(request))
                    .header("authorization-token", "validTokenWithUserId"))
                .andExpect(status().isConflict())
                .andExpect(
                    jsonPath("$.local_message").value(ErrorCodes.REVIEW_POST_EXIST.message))
                .andExpect(jsonPath("$.code").value(ErrorCodes.REVIEW_POST_EXIST.code))
            ;
        }


        @DisplayName("[성공] 후기 작성 - 201 Created 반환")
        @Test
        void test1000() throws Exception {
            Long articleId = 1L;

            ReviewSaveRequest request = ReviewSaveRequest.builder()
                .articleId(articleId)
                .title("review_title")
                .representativeImgUrl("https://review_img.png")
                .description("review_description")
                .status(ReviewStatus.ACTIVE)
                .build();

            Review review = Review.builder()
                .id(1L)
                .user(user)
                .article(article)
                .title("review_title")
                .representativeImgUrl("https://review_img.png")
                .description("review_description")
                .status(ReviewStatus.ACTIVE)
                .build();

            given(reviewWriterService.save(eq(1L), any(ReviewSaveRequest.class))).willReturn(
                review);

            mockMvc.perform(post("/api/v1/review")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(request))
                    .header("authorization-token", "validTokenWithUserId"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.review_id").value(review.getId()));
        }
    }
}
