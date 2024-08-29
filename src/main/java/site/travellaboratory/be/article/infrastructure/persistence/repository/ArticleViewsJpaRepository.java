package site.travellaboratory.be.article.infrastructure.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.travellaboratory.be.article.infrastructure.persistence.entity.ArticleViewsEntity;

public interface ArticleViewsJpaRepository extends JpaRepository<ArticleViewsEntity, Long> {
    Optional<ArticleViewsEntity> findByUserIdAndArticleIdAndCreatedAtBetween(Long userId, Long articleId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT v.articleId FROM ArticleViewsEntity v WHERE v.createdAt >= :oneMonthAgo GROUP BY v.articleId ORDER BY COUNT(v.id) DESC ")

    List<Long> findTopArticleIdsByViewsCount(@Param("oneMonthAgo") LocalDateTime oneMonthAgo, Pageable pageable);

}
