package site.travellaboratory.be.infrastructure.domains.bookmark;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.travellaboratory.be.infrastructure.domains.article.entity.Article;
import site.travellaboratory.be.infrastructure.domains.bookmark.entity.Bookmark;
import site.travellaboratory.be.infrastructure.domains.bookmark.enums.BookmarkStatus;
import site.travellaboratory.be.infrastructure.domains.user.entity.UserJpaEntity;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Optional<Bookmark> findByArticleAndUserJpaEntity(final Article article, final UserJpaEntity userJpaEntity);

    Optional<List<Bookmark>> findByUserJpaEntityAndStatusIn(final UserJpaEntity userJpaEntity, final List<BookmarkStatus> Status);

//    List<Bookmark> findByUserAndStatus(final User user, final BookmarkStatus status);

    Optional<Page<Bookmark>> findByUserJpaEntityAndStatusIn(final UserJpaEntity userJpaEntity, final List<BookmarkStatus> Status, Pageable pageable);

    @Query("SELECT COUNT(b) FROM Bookmark b WHERE b.article.id = :articleId AND b.status = :status")
    Long countByArticleIdAndStatus(@Param("articleId") Long articleId, @Param("status") BookmarkStatus status);

    boolean existsByUserJpaEntityIdAndArticleIdAndStatus(Long loginId, Long articleId, BookmarkStatus active);
}
