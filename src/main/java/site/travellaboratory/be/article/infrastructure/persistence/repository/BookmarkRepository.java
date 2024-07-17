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
import site.travellaboratory.be.article.infrastructure.persistence.entity.Bookmark;
import site.travellaboratory.be.article.domain.enums.BookmarkStatus;
import site.travellaboratory.be.user.infrastructure.persistence.entity.UserEntity;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Optional<Bookmark> findByArticleEntityAndUserEntity(final ArticleEntity articleEntity, final UserEntity userEntity);

    Optional<List<Bookmark>> findByUserEntityAndStatusIn(final UserEntity userEntity, final List<BookmarkStatus> Status);

//    List<Bookmark> findByUserAndStatus(final User user, final BookmarkStatus status);

    Optional<Page<Bookmark>> findByUserEntityAndStatusIn(final UserEntity userEntity, final List<BookmarkStatus> Status, Pageable pageable);

    @Query("SELECT COUNT(b) FROM Bookmark b WHERE b.articleEntity.id = :articleId AND b.status = :status")
    Long countByArticleIdAndStatus(@Param("articleId") Long articleId, @Param("status") BookmarkStatus status);

    boolean existsByUserEntityIdAndArticleEntityIdAndStatus(Long loginId, Long articleId, BookmarkStatus active);
}
