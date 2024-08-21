package site.travellaboratory.be.review.infrastructure.persistence.repository;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.travellaboratory.be.article.domain.Article;
import site.travellaboratory.be.article.infrastructure.persistence.entity.ArticleEntity;
import site.travellaboratory.be.review.application.port.ReviewRepository;
import site.travellaboratory.be.review.domain.Review;
import site.travellaboratory.be.review.domain.enums.ReviewStatus;
import site.travellaboratory.be.review.infrastructure.persistence.entity.ReviewEntity;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {

    private final ReviewJpaRepository reviewJpaRepository;

    @Override
    public Optional<Review> findByArticleAndStatusOrderByArticleDesc(Article article, ReviewStatus status) {
        return reviewJpaRepository.findByArticleEntityAndStatus(ArticleEntity.from(article), status)
            .map(ReviewEntity::toModel);
    }

    @Override
    public Optional<Review> findByIdAndStatus(Long reviewId, ReviewStatus status) {
        return reviewJpaRepository.findByIdAndStatus(reviewId, status)
            .map(ReviewEntity::toModel);
    }

    @Override
    public Review save(Review review) {
        return reviewJpaRepository.save(ReviewEntity.from(review)).toModel();
    }


}
