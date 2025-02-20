package site.travellaboratory.be.article.infrastructure.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.travellaboratory.be.article.domain.enums.BookmarkStatus;
import site.travellaboratory.be.article.infrastructure.persistence.entity.ArticleEntity;
import site.travellaboratory.be.article.infrastructure.persistence.entity.BookmarkEntity;
import site.travellaboratory.be.user.infrastructure.persistence.entity.UserEntity;

@Repository
public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long> {

    Optional<BookmarkEntity> findByArticleEntityAndUserEntity(final ArticleEntity articleEntity, final UserEntity userEntity);

    List<BookmarkEntity> findByUserEntityAndStatus(final UserEntity userEntity, final BookmarkStatus Status);

    Page<BookmarkEntity> findByUserEntityAndStatusOrderByCreatedAtDesc(final UserEntity userEntity, final BookmarkStatus Status, Pageable pageable);

    @Query("SELECT COUNT(b) FROM BookmarkEntity b WHERE b.articleEntity.id = :articleId AND b.status = :status")
    Long countByArticleIdAndStatus(@Param("articleId") Long articleId, @Param("status") BookmarkStatus status);

    boolean existsByUserEntityIdAndArticleEntityIdAndStatus(Long loginId, Long articleId, BookmarkStatus active);

    @Query("SELECT bm.articleEntity.id FROM BookmarkEntity bm WHERE bm.createdAt >= :standardLocalDateTime and bm.status = 'ACTIVE' GROUP BY bm.articleEntity.id ORDER BY COUNT(bm.id)DESC")
    List<Long> findTopArticleIdsByLikeCount(@Param("standardLocalDateTime") LocalDateTime localDateTime, Pageable pageable);
}
