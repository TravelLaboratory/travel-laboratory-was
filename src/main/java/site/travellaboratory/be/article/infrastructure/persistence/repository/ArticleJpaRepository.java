package site.travellaboratory.be.article.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.travellaboratory.be.article.infrastructure.persistence.entity.ArticleEntity;
import site.travellaboratory.be.article.domain.enums.ArticleStatus;
import site.travellaboratory.be.user.infrastructure.persistence.entity.UserEntity;

@Repository
public interface ArticleJpaRepository extends JpaRepository<ArticleEntity, Long> {

    Optional<ArticleEntity> findByIdAndStatusIn(final Long articleId, List<ArticleStatus> Status);

    List<ArticleEntity> findAllByStatus(ArticleStatus status);

    @Query("SELECT a from ArticleEntity a WHERE a.status = :status order by a.createdAt desc")
    Page<ArticleEntity> findAllByStatusOrderByCreatedAtDesc(ArticleStatus status, Pageable pageable);

    Optional<List<ArticleEntity>> findByUserEntityAndStatusIn(UserEntity userEntity, List<ArticleStatus> status);

    Optional<Page<ArticleEntity>> findByUserEntityAndStatusIn(UserEntity userEntity, List<ArticleStatus> status, Pageable pageable);

    @Query("SELECT a FROM ArticleEntity a JOIN a.locationEntities l WHERE l.city LIKE %:keyword% AND a.status = :status")
    List<ArticleEntity> findByLocationCityContainingAndStatusActive(
            @Param("keyword") String keyword,
            @Param("status") ArticleStatus status);

    @Query("SELECT a FROM ArticleEntity a JOIN a.locationEntities l WHERE l.city LIKE %:keyword% AND a.status = :status")
    Page<ArticleEntity> findByLocationCityContainingAndStatusActive(
            @Param("keyword") String keyword,
            Pageable pageable,
            @Param("status") ArticleStatus status);
}
