package site.travellaboratory.be.application.review;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.domain.review.Review;
import site.travellaboratory.be.domain.review.enums.ReviewStatus;
import site.travellaboratory.be.infrastructure.domains.article.ArticleRepository;
import site.travellaboratory.be.infrastructure.domains.article.entity.Article;
import site.travellaboratory.be.infrastructure.domains.article.enums.ArticleStatus;
import site.travellaboratory.be.infrastructure.domains.review.ReviewRepository;
import site.travellaboratory.be.infrastructure.domains.review.entity.ReviewJpaEntity;
import site.travellaboratory.be.presentation.review.dto.writer.ReviewSaveRequest;
import site.travellaboratory.be.presentation.review.dto.writer.ReviewUpdateRequest;

@Service
@RequiredArgsConstructor
public class ReviewWriterService {

    private final ReviewRepository reviewRepository;
    private final ArticleRepository articleRepository;

    @Transactional
    public Long saveReview(Long userId, ReviewSaveRequest request) {
        // 유효하지 않은 여행 계획에 대한 후기를 작성할 경우
        Article article = articleRepository.findByIdAndStatusIn(request.articleId(),
                List.of(ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.REVIEW_POST_INVALID,
                HttpStatus.NOT_FOUND));

        // 이미 해당 여행 계획에 대한 후기가 있을 경우
        reviewRepository.findByArticleAndStatusInOrderByArticleDesc(article,
                List.of(ReviewStatus.ACTIVE, ReviewStatus.PRIVATE))
            .ifPresent(it -> {
                throw new BeApplicationException(ErrorCodes.REVIEW_POST_EXIST,
                    HttpStatus.CONFLICT);
            });

        // Review 클래스에서 검증하고 생성
        Review review = Review.create(userId, article.getUser(), article, request.title(),
            request.representativeImgUrl(),
            request.description(), request.status());

        Review saveReview = reviewRepository.save(ReviewJpaEntity.from(review)).toModel();
        return saveReview.getId();
    }

    @Transactional
    public Long updateReview(Long userId, Long reviewId,
        ReviewUpdateRequest request) {
        // 유효하지 않은 후기를 수정할 경우
        Review review = reviewRepository.findByIdAndStatusIn(reviewId,
                List.of(ReviewStatus.ACTIVE, ReviewStatus.PRIVATE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.REVIEW_UPDATE_INVALID,
                HttpStatus.NOT_FOUND)).toModel();

        Review updatedReview = review.withUpdatedContent(userId, request.title(),
            request.representativeImgUrl(),
            request.description(), request.status());

        // 후기 업데이트
        return reviewRepository.save(ReviewJpaEntity.from(updatedReview)).toModel().getId();
    }

    @Transactional
    public boolean deleteReview(final Long userId, final Long reviewId) {
        // 유효하지 않은 후기를 삭제할 경우
        Review review = reviewRepository.findByIdAndStatusIn(reviewId,
                List.of(ReviewStatus.ACTIVE, ReviewStatus.PRIVATE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.REVIEW_DELETE_INVALID,
                HttpStatus.NOT_FOUND)).toModel();

        // 삭제
        Review deletedReview = review.withInactiveStatus(userId);
        Review result = reviewRepository.save(ReviewJpaEntity.from(deletedReview)).toModel();
        return result.getStatus().equals(ReviewStatus.INACTIVE);
    }
}
