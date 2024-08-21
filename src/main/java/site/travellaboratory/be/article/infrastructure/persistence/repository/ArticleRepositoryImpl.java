package site.travellaboratory.be.article.infrastructure.persistence.repository;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.travellaboratory.be.article.application.port.ArticleRepository;
import site.travellaboratory.be.article.domain.Article;
import site.travellaboratory.be.article.domain.enums.ArticleStatus;
import site.travellaboratory.be.article.infrastructure.persistence.entity.ArticleEntity;

@Repository
@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepository {

    private final ArticleJpaRepository articleJpaRepository;

    @Override
    public Optional<Article> findByIdAndStatus(Long articleId, ArticleStatus Status) {
        return articleJpaRepository.findByIdAndStatus(articleId, Status)
            .map(ArticleEntity::toModel);
    }

    @Override
    public Article save(Article article) {
        return articleJpaRepository.save(ArticleEntity.from(article)).toModel();
    }
}
