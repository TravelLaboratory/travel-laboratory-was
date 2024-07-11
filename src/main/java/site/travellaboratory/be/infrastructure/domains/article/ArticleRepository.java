package site.travellaboratory.be.infrastructure.domains.article;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.travellaboratory.be.infrastructure.domains.article.entity.Article;
import site.travellaboratory.be.infrastructure.domains.article.enums.ArticleStatus;
import site.travellaboratory.be.infrastructure.domains.user.entity.User;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    Optional<Article> findByIdAndStatusIn(final Long articleId, List<ArticleStatus> Status);

    List<Article> findAllByStatus(ArticleStatus status);

    Optional<List<Article>> findByUserAndStatusIn(User user, List<ArticleStatus> status);

    Optional<Page<Article>> findByUserAndStatusIn(User user, List<ArticleStatus> status, Pageable pageable);

    @Query("SELECT a FROM Article a JOIN a.location l WHERE l.city LIKE %:keyword% AND a.status = :status")
    List<Article> findByLocationCityContainingAndStatusActive(
            @Param("keyword") String keyword,
            @Param("status") ArticleStatus status);

    @Query("SELECT a FROM Article a JOIN a.location l WHERE l.city LIKE %:keyword% AND a.status = :status")
    Page<Article> findByLocationCityContainingAndStatusActive(
            @Param("keyword") String keyword,
            Pageable pageable,
            @Param("status") ArticleStatus status);
}
