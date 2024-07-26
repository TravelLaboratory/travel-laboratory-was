package site.travellaboratory.be.review.infrastructure.persistence.repository;

import java.util.List;
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
    public Optional<Review> findByArticleAndStatusInOrderByArticleDesc(Article article, List<ReviewStatus> status) {
        return reviewJpaRepository.findByArticleEntityAndStatusIn(ArticleEntity.from(article), status)
            .map(ReviewEntity::toModel);
    }

    @Override
    public Optional<Review> findByIdAndStatusIn(Long reviewId, List<ReviewStatus> status) {
        return reviewJpaRepository.findByIdAndStatusIn(reviewId, status)
            .map(ReviewEntity::toModel);
    }

    @Override
    public Review save(Review review) {
        return reviewJpaRepository.save(ReviewEntity.from(review)).toModel();
    }


}
