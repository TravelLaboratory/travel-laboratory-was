package site.travellaboratory.be.review.application.port;

import java.util.Optional;
import site.travellaboratory.be.article.domain.Article;
import site.travellaboratory.be.review.domain.Review;
import site.travellaboratory.be.review.domain.enums.ReviewStatus;

public interface ReviewRepository {

    Optional<Review> findByArticleAndStatusOrderByArticleDesc(Article article, ReviewStatus status);

    Optional<Review> findByIdAndStatus(Long reviewId, ReviewStatus status);

    Review save(Review review);
}
