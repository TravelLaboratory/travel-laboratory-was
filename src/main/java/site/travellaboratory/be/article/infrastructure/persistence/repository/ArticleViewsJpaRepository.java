package site.travellaboratory.be.article.infrastructure.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.travellaboratory.be.article.infrastructure.persistence.entity.ArticleViewsEntity;

public interface ArticleViewsJpaRepository extends JpaRepository<ArticleViewsEntity, Long> {
    Optional<ArticleViewsEntity> findByUserIdAndArticleIdAndCreatedAtBetween(Long userId, Long articleId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT v.articleId FROM ArticleViewsEntity v WHERE v.createdAt >= :standardLocalDateTime GROUP BY v.articleId ORDER BY COUNT(v.id) DESC ")

    List<Long> findTopArticleIdsByViewsCount(Pageable pageable);

}
