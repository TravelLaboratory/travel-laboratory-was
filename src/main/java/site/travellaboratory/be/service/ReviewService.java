package site.travellaboratory.be.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.controller.review.dto.ReviewSaveRequest;
import site.travellaboratory.be.controller.review.dto.ReviewSaveResponse;
import site.travellaboratory.be.domain.article.Article;
import site.travellaboratory.be.domain.article.ArticleRepository;
import site.travellaboratory.be.domain.article.ArticleStatus;
import site.travellaboratory.be.domain.review.Review;
import site.travellaboratory.be.domain.review.ReviewRepository;
import site.travellaboratory.be.domain.review.ReviewStatus;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ArticleRepository articleRepository;

    @Transactional
    public ReviewSaveResponse saveReview(Long userId, Long articleId, ReviewSaveRequest request) {
        // 삭제된 여행 계획에 대한 후기를 작성할 경우
        Article article = articleRepository.findByIdAndStatusIn(articleId, List.of(ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.REVIEW_INVALID_ARTICLE,
                HttpStatus.NOT_FOUND));

        // 유저가 작성한 article_id이 아닌 경우
        if (!article.getUser().getId().equals(userId)) {
            throw new BeApplicationException(ErrorCodes.REVIEW_NOT_USER_ARTICLE, HttpStatus.FORBIDDEN);
        }

        // 이미 해당 여행 계획에 대한 후기가 있을 경우
        reviewRepository.findByArticleAndStatusNotOrderByArticleDesc(article, ReviewStatus.INACTIVE)
            .ifPresent(it -> {
                throw new BeApplicationException(ErrorCodes.REVIEW_EXIST_USER_ARTICLE,
                    HttpStatus.CONFLICT);
            });

        // 본인이 작성한 여행 계획 + 후기가 없는 경우
        Review saveEntity = reviewRepository.save(
            Review.of(
                article.getUser(),
                article,
                request.title(),
                request.representativeImgUrl(),
                request.description()
            )
        );
        return ReviewSaveResponse.from(saveEntity.getId());
    }
}
