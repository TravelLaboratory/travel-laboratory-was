package site.travellaboratory.be.application.review;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.infrastructure.domains.article.ArticleRepository;
import site.travellaboratory.be.infrastructure.domains.article.entity.Article;
import site.travellaboratory.be.infrastructure.domains.article.enums.ArticleStatus;
import site.travellaboratory.be.infrastructure.domains.review.ReviewRepository;
import site.travellaboratory.be.infrastructure.domains.review.entity.Review;
import site.travellaboratory.be.infrastructure.domains.review.enums.ReviewStatus;
import site.travellaboratory.be.presentation.review.dto.writer.ReviewDeleteResponse;
import site.travellaboratory.be.presentation.review.dto.writer.ReviewSaveRequest;
import site.travellaboratory.be.presentation.review.dto.writer.ReviewSaveResponse;
import site.travellaboratory.be.presentation.review.dto.writer.ReviewUpdateRequest;
import site.travellaboratory.be.presentation.review.dto.writer.ReviewUpdateResponse;

@Service
@RequiredArgsConstructor
public class ReviewWriterService {

    private final ReviewRepository reviewRepository;
    private final ArticleRepository articleRepository;

    @Transactional
    public ReviewSaveResponse saveReview(Long userId, ReviewSaveRequest request) {
        // 유효하지 않은 여행 계획에 대한 후기를 작성할 경우
        Article article = articleRepository.findByIdAndStatusIn(request.articleId(),
                List.of(ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.REVIEW_POST_INVALID,
                HttpStatus.NOT_FOUND));

        // 유저가 작성한 article_id이 아닌 경우
        if (!article.getUser().getId().equals(userId)) {
            throw new BeApplicationException(ErrorCodes.REVIEW_POST_NOT_USER, HttpStatus.FORBIDDEN);
        }

        // 이미 해당 여행 계획에 대한 후기가 있을 경우
        reviewRepository.findByArticleAndStatusInOrderByArticleDesc(article,
                List.of(ReviewStatus.ACTIVE, ReviewStatus.PRIVATE))
            .ifPresent(it -> {
                throw new BeApplicationException(ErrorCodes.REVIEW_POST_EXIST,
                    HttpStatus.CONFLICT);
            });

        // 본인이 작성한 여행 계획 + 후기가 없는 경우
        Review saveReview = reviewRepository.save(
            Review.of(
                article.getUser(),
                article,
                request.title(),
                request.representativeImgUrl(),
                request.description(),
                request.status()
            )
        );
        return ReviewSaveResponse.from(saveReview.getId());
    }

    @Transactional
    public ReviewUpdateResponse updateReview(Long userId, Long reviewId,
        ReviewUpdateRequest request) {
        // 유효하지 않은 후기를 수정할 경우
        Review review = reviewRepository.findByIdAndStatusIn(reviewId,
                List.of(ReviewStatus.ACTIVE, ReviewStatus.PRIVATE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.REVIEW_UPDATE_INVALID,
                HttpStatus.NOT_FOUND));

        // 유저가 작성한 후기가 아닌 경우
        if (!review.getUser().getId().equals(userId)) {
            throw new BeApplicationException(ErrorCodes.REVIEW_UPDATE_NOT_USER,
                HttpStatus.FORBIDDEN);
        }

        // 후기 업데이트
        review.update(request.title(), request.representativeImgUrl(), request.description(),
            request.status());
        Review updateReview = reviewRepository.save(review);
        return ReviewUpdateResponse.from(updateReview.getId());
    }

    @Transactional
    public ReviewDeleteResponse deleteReview(final Long userId, final Long reviewId) {
        // 유효하지 않은 후기를 삭제할 경우
        Review review = reviewRepository.findByIdAndStatusIn(reviewId,
                List.of(ReviewStatus.ACTIVE, ReviewStatus.PRIVATE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.REVIEW_DELETE_INVALID,
                HttpStatus.NOT_FOUND));

        // 유저가 작성한 후기가 아닌 경우
        if (!review.getUser().getId().equals(userId)) {
            throw new BeApplicationException(ErrorCodes.REVIEW_DELETE_NOT_USER,
                HttpStatus.FORBIDDEN);
        }

        // 후기 삭제
        review.delete();
        reviewRepository.save(review);
        return ReviewDeleteResponse.from(true);
    }
}
