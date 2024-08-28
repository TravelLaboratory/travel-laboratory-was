package site.travellaboratory.be.article.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.travellaboratory.be.article.infrastructure.persistence.entity.ArticleViewsEntity;

public interface ArticleViewsJpaRepository extends JpaRepository<ArticleViewsEntity, Long> {

}
