package site.travellaboratory.be.domain.review;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import site.travellaboratory.be.domain.article.Article;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByArticleAndStatusNotOrderByArticleDesc(Article article, ReviewStatus status);
}
