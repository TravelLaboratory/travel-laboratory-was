package site.travellaboratory.be.article.application.port;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.travellaboratory.be.article.domain.Article;
import site.travellaboratory.be.article.domain.enums.ArticleStatus;
import site.travellaboratory.be.user.domain.User;

public interface ArticleRepository {

    Optional<Article> findByIdAndStatusIn(final Long articleId, List<ArticleStatus> Status);

    List<Article> findAllByStatus(ArticleStatus status);

    Page<Article> findAllByStatusOrderByCreatedAtDesc(ArticleStatus status, Pageable pageable);

    Optional<List<Article>> findByUserEntityAndStatusIn(User user, List<ArticleStatus> status);

    Optional<Page<Article>> findByUserEntityAndStatusIn(User user, List<ArticleStatus> status, Pageable pageable);

    List<Article> findByLocationCityContainingAndStatusActive(String keyword, ArticleStatus status);

    Page<Article> findByLocationCityContainingAndStatusActive(String keyword, Pageable pageable, ArticleStatus status);
}
