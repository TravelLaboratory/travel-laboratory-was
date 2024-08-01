package site.travellaboratory.be.review.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import site.travellaboratory.be.user.application.port.UserRepository;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain.enums.UserStatus;

@Service
@RequiredArgsConstructor
public class ReviewWriterService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;


    @Transactional
    public Review save(Long userId, ReviewSaveRequest request) {
        Article article = getArticleById(request.articleId());
        verifyExistReviewBy(article);
        User user = getUserById(userId);

        // Review 클래스에서 검증하고 생성
        Review review = Review.create(user, article, request);
        return reviewRepository.save(review);
    }


    @Transactional
    public Review update(Long userId, Long reviewId, ReviewUpdateRequest request) {
        Review review = getReviewById(reviewId);
        User user = getUserById(userId);

        // 후기 업데이트
        Review updatedReview = review.withUpdatedContent(user, request);
        return reviewRepository.save(updatedReview);
    }

    @Transactional
    public Review delete(Long userId, Long reviewId) {
        Review review = getReviewById(reviewId);
        User user = getUserById(userId);

        // 후기 삭제
        Review deletedReview = review.withInactiveStatus(user);
        return reviewRepository.save(deletedReview);
    }

    private Article getArticleById(Long articleId) {
        return articleRepository.findByIdAndStatusIn(articleId, List.of(ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.REVIEW_INVALID_ARTICLE_ID, HttpStatus.NOT_FOUND));
    }

    private User getUserById(Long userId) {
        return userRepository.getByIdAndStatus(userId, UserStatus.ACTIVE);
    }

    private Review getReviewById(Long reviewId) {
        return reviewRepository.findByIdAndStatusIn(reviewId, List.of(ReviewStatus.ACTIVE, ReviewStatus.PRIVATE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.REVIEW_INVALID_REVIEW_ID,
                HttpStatus.NOT_FOUND));
    }

    // 이미 해당 여행 계획에 대한 후기가 있을 경우
    private void verifyExistReviewBy(Article article) {
        reviewRepository.findByArticleAndStatusInOrderByArticleDesc(article,
                List.of(ReviewStatus.ACTIVE, ReviewStatus.PRIVATE))
            .ifPresent(
                it -> {
                    throw new BeApplicationException(ErrorCodes.REVIEW_POST_EXIST,
                        HttpStatus.CONFLICT);
                });
    }
}