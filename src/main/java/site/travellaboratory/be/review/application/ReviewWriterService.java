package site.travellaboratory.be.review.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.article.domain.Article;
import site.travellaboratory.be.article.domain.enums.ArticleStatus;
import site.travellaboratory.be.review.domain.Review;
import site.travellaboratory.be.review.domain.enums.ReviewStatus;
import site.travellaboratory.be.user.domain.enums.UserStatus;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.article.infrastructure.persistence.repository.ArticleJpaRepository;
import site.travellaboratory.be.article.infrastructure.persistence.entity.ArticleEntity;
import site.travellaboratory.be.review.infrastructure.persistence.ReviewEntity;
import site.travellaboratory.be.review.infrastructure.persistence.ReviewJpaRepository;
import site.travellaboratory.be.user.infrastructure.persistence.repository.UserJpaRepository;
import site.travellaboratory.be.review.domain.request.ReviewSaveRequest;
import site.travellaboratory.be.review.domain.request.ReviewUpdateRequest;

@Service
@RequiredArgsConstructor
public class ReviewWriterService {

    private final ReviewJpaRepository reviewJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final ArticleJpaRepository articleJpaRepository;


    @Transactional
    public Long saveReview(Long userId, ReviewSaveRequest request) {
        // 유효하지 않은 여행 계획에 대한 후기를 작성할 경우
        Article article = articleJpaRepository.findByIdAndStatusIn(request.articleId(),
                List.of(ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.REVIEW_POST_INVALID,
                HttpStatus.NOT_FOUND)).toModel();

        // 이미 해당 여행 계획에 대한 후기가 있을 경우
        reviewJpaRepository.findByArticleEntityAndStatusInOrderByArticleEntityDesc(
                ArticleEntity.from(article),
                List.of(ReviewStatus.ACTIVE, ReviewStatus.PRIVATE))
            .ifPresent(it -> {
                throw new BeApplicationException(ErrorCodes.REVIEW_POST_EXIST,
                    HttpStatus.CONFLICT);
            });

        User user = userJpaRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND, HttpStatus.NOT_FOUND)).toModel();

        // Review 클래스에서 검증하고 생성
        Review review = Review.create(user, article, request);
        Review saveReview = reviewJpaRepository.save(ReviewEntity.from(review)).toModel();
        return saveReview.getId();
    }

    @Transactional
    public Long updateReview(Long userId, Long reviewId, ReviewUpdateRequest request) {
        // 유효하지 않은 후기를 수정할 경우
        Review review = reviewJpaRepository.findByIdAndStatusIn(reviewId,
                List.of(ReviewStatus.ACTIVE, ReviewStatus.PRIVATE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.REVIEW_UPDATE_INVALID,
                HttpStatus.NOT_FOUND)).toModel();

        User user = userJpaRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND, HttpStatus.NOT_FOUND)).toModel();

        // 후기 업데이트
        Review updatedReview = review.withUpdatedContent(user, request);
        reviewJpaRepository.save(ReviewEntity.from(updatedReview)).toModel();
        return updatedReview.getId();
    }

    @Transactional
    public boolean deleteReview(final Long userId, final Long reviewId) {
        // 유효하지 않은 후기를 삭제할 경우
        Review review = reviewJpaRepository.findByIdAndStatusIn(reviewId,
                List.of(ReviewStatus.ACTIVE, ReviewStatus.PRIVATE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.REVIEW_DELETE_INVALID,
                HttpStatus.NOT_FOUND)).toModel();

        User user = userJpaRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND, HttpStatus.NOT_FOUND)).toModel();

        // 삭제
        Review deletedReview = review.withInactiveStatus(user);
        Review result = reviewJpaRepository.save(ReviewEntity.from(deletedReview)).toModel();
        return result.getStatus() == ReviewStatus.INACTIVE;
    }
}
