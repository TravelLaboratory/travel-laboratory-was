package site.travellaboratory.be.article.application.port;

import java.util.Optional;
import site.travellaboratory.be.article.domain.Article;
import site.travellaboratory.be.article.domain.enums.ArticleStatus;

public interface ArticleRepository {

    Optional<Article> findByIdAndStatus(final Long articleId, ArticleStatus Status);
    Article save(Article article);
}
