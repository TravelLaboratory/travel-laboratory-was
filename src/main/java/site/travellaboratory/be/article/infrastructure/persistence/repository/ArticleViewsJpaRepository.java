package site.travellaboratory.be.article.infrastructure.persistence.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import site.travellaboratory.be.article.infrastructure.persistence.entity.ArticleViewsEntity;

public interface ArticleViewsJpaRepository extends JpaRepository<ArticleViewsEntity, Long> {
    Optional<ArticleViewsEntity> findByUserIdAndArticleIdAndUpdatedAtBetween(Long userId, Long articleId, LocalDateTime start, LocalDateTime end);
}
