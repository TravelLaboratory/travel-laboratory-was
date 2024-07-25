package site.travellaboratory.be.article.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import site.travellaboratory.be.article.application.port.ArticleRepository;
import site.travellaboratory.be.article.domain.Article;
import site.travellaboratory.be.article.domain.enums.ArticleStatus;
import site.travellaboratory.be.article.infrastructure.persistence.entity.ArticleEntity;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.infrastructure.persistence.entity.UserEntity;

@Repository
@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepository {

    private final ArticleJpaRepository articleJpaRepository;

    @Override
    public Optional<Article> findByIdAndStatusIn(Long articleId, List<ArticleStatus> Status) {
        return articleJpaRepository.findByIdAndStatusIn(articleId, Status)
            .map(ArticleEntity::toModel);
    }

    @Override
    public List<Article> findAllByStatus(ArticleStatus status) {
        return articleJpaRepository.findAllByStatus(status).stream()
            .map(ArticleEntity::toModel).toList();
    }

    @Override
    public Page<Article> findAllByStatusOrderByCreatedAtDesc(ArticleStatus status, Pageable pageable) {
        return articleJpaRepository.findAllByStatusOrderByCreatedAtDesc(status, pageable)
            .map(ArticleEntity::toModel);
    }

    @Override
    public Optional<List<Article>> findByUserEntityAndStatusIn(User user, List<ArticleStatus> status) {
        return articleJpaRepository.findByUserEntityAndStatusIn(UserEntity.from(user), status)
            .map(entities -> entities.stream().map(ArticleEntity::toModel).toList());
    }

    @Override
    public Optional<Page<Article>> findByUserEntityAndStatusIn(User user, List<ArticleStatus> status, Pageable pageable) {
        return articleJpaRepository.findByUserEntityAndStatusIn(UserEntity.from(user), status, pageable)
            .map(page -> page.map(ArticleEntity::toModel));
    }

    @Override
    public List<Article> findByLocationCityContainingAndStatusActive(String keyword, ArticleStatus status) {
        return articleJpaRepository.findByLocationCityContainingAndStatusActive(keyword, status).stream()
            .map(ArticleEntity::toModel).toList();
    }

    @Override
    public Page<Article> findByLocationCityContainingAndStatusActive(String keyword, Pageable pageable, ArticleStatus status) {
        return articleJpaRepository.findByLocationCityContainingAndStatusActive(keyword, pageable, status)
            .map(ArticleEntity::toModel);
    }
}
