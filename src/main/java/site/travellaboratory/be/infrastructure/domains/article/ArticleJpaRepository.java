package site.travellaboratory.be.infrastructure.domains.article;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.travellaboratory.be.infrastructure.domains.article.entity.ArticleJpaEntity;
import site.travellaboratory.be.infrastructure.domains.article.enums.ArticleStatus;
import site.travellaboratory.be.infrastructure.domains.user.entity.UserJpaEntity;

@Repository
public interface ArticleJpaRepository extends JpaRepository<ArticleJpaEntity, Long> {

    Optional<ArticleJpaEntity> findByIdAndStatusIn(final Long articleId, List<ArticleStatus> Status);

    List<ArticleJpaEntity> findAllByStatus(ArticleStatus status);

    @Query("SELECT a from ArticleJpaEntity a WHERE a.status = :status order by a.createdAt desc")
    Page<ArticleJpaEntity> findAllByStatusOrderByCreatedAtDesc(ArticleStatus status, Pageable pageable);

    Optional<List<ArticleJpaEntity>> findByUserJpaEntityAndStatusIn(UserJpaEntity userJpaEntity, List<ArticleStatus> status);

    Optional<Page<ArticleJpaEntity>> findByUserJpaEntityAndStatusIn(UserJpaEntity userJpaEntity, List<ArticleStatus> status, Pageable pageable);

    @Query("SELECT a FROM ArticleJpaEntity a JOIN a.location l WHERE l.city LIKE %:keyword% AND a.status = :status")
    List<ArticleJpaEntity> findByLocationCityContainingAndStatusActive(
            @Param("keyword") String keyword,
            @Param("status") ArticleStatus status);

    @Query("SELECT a FROM ArticleJpaEntity a JOIN a.location l WHERE l.city LIKE %:keyword% AND a.status = :status")
    Page<ArticleJpaEntity> findByLocationCityContainingAndStatusActive(
            @Param("keyword") String keyword,
            Pageable pageable,
            @Param("status") ArticleStatus status);
}
