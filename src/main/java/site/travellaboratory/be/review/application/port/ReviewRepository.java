package site.travellaboratory.be.review.application.port;

import java.util.List;
import java.util.Optional;
import site.travellaboratory.be.article.domain.Article;
import site.travellaboratory.be.review.domain.Review;
import site.travellaboratory.be.review.domain.enums.ReviewStatus;

public interface ReviewRepository {

    Optional<Review> findByArticleAndStatusInOrderByArticleDesc(Article article, List<ReviewStatus> status);

    Optional<Review> findByIdAndStatusIn(Long reviewId, List<ReviewStatus> status);

    Review save(Review review);
}
