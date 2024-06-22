package site.travellaboratory.be.domain.review;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import site.travellaboratory.be.domain.article.Article;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByArticleAndStatusInOrderByArticleDesc(Article article, List<ReviewStatus> status);

    Optional<Review> findByIdAndStatusIn(Long reviewId, List<ReviewStatus> status);

    // 일정 상세에서 reviewId 찾아오기
    Optional<Review> findByArticleAndStatus(Article article, ReviewStatus status);
}
