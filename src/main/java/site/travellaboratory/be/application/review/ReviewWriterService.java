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
import site.travellaboratory.be.infrastructure.domains.article.ArticleJpaRepository;
import site.travellaboratory.be.infrastructure.domains.article.entity.ArticleJpaEntity;
import site.travellaboratory.be.infrastructure.domains.article.enums.ArticleStatus;
import site.travellaboratory.be.infrastructure.domains.review.repository.ReviewJpaRepository;
import site.travellaboratory.be.infrastructure.domains.review.entity.ReviewJpaEntity;
import site.travellaboratory.be.infrastructure.domains.user.UserJpaRepository;
import site.travellaboratory.be.infrastructure.domains.user.entity.UserJpaEntity;
import site.travellaboratory.be.domain.user.enums.UserStatus;
import site.travellaboratory.be.presentation.review.dto.writer.ReviewSaveRequest;
import site.travellaboratory.be.presentation.review.dto.writer.ReviewUpdateRequest;

@Service
@RequiredArgsConstructor
public class ReviewWriterService {

    private final ReviewJpaRepository reviewJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final ArticleJpaRepository articleJpaRepository;


    @Transactional
    public Long saveReview(Long userId, ReviewSaveRequest request) {
        // 유효하지 않은 여행 계획에 대한 후기를 작성할 경우
        ArticleJpaEntity articleJpaEntity = articleJpaRepository.findByIdAndStatusIn(request.articleId(),
                List.of(ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.REVIEW_POST_INVALID,
                HttpStatus.NOT_FOUND));

        // 이미 해당 여행 계획에 대한 후기가 있을 경우
        reviewJpaRepository.findByArticleJpaEntityAndStatusInOrderByArticleJpaEntityDesc(articleJpaEntity,
                List.of(ReviewStatus.ACTIVE, ReviewStatus.PRIVATE))
            .ifPresent(it -> {
                throw new BeApplicationException(ErrorCodes.REVIEW_POST_EXIST,
                    HttpStatus.CONFLICT);
            });

        UserJpaEntity userJpaEntity = userJpaRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        // Review 클래스에서 검증하고 생성
        Review review = Review.create(userJpaEntity, articleJpaEntity,
            request.title(), request.representativeImgUrl(),
            request.description(), request.status());

        Review saveReview = reviewJpaRepository.save(ReviewJpaEntity.from(review)).toModel();
        return saveReview.getId();
    }

    @Transactional
    public Long updateReview(Long userId, Long reviewId,
        ReviewUpdateRequest request) {
        // 유효하지 않은 후기를 수정할 경우
        Review review = reviewJpaRepository.findByIdAndStatusIn(reviewId,
                List.of(ReviewStatus.ACTIVE, ReviewStatus.PRIVATE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.REVIEW_UPDATE_INVALID,
                HttpStatus.NOT_FOUND)).toModel();

        UserJpaEntity userJpaEntity = userJpaRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        Review updatedReview = review.withUpdatedContent(userJpaEntity, request.title(),
            request.representativeImgUrl(),
            request.description(), request.status());

        // 후기 업데이트
        return reviewJpaRepository.save(ReviewJpaEntity.from(updatedReview)).toModel().getId();
    }

    @Transactional
    public boolean deleteReview(final Long userId, final Long reviewId) {
        // 유효하지 않은 후기를 삭제할 경우
        Review review = reviewJpaRepository.findByIdAndStatusIn(reviewId,
                List.of(ReviewStatus.ACTIVE, ReviewStatus.PRIVATE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.REVIEW_DELETE_INVALID,
                HttpStatus.NOT_FOUND)).toModel();

        UserJpaEntity userJpaEntity = userJpaRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        // 삭제
        Review deletedReview = review.withInactiveStatus(userJpaEntity);
        Review result = reviewJpaRepository.save(ReviewJpaEntity.from(deletedReview)).toModel();
        return result.getStatus() == ReviewStatus.INACTIVE;
    }
}
