package site.travellaboratory.be.domain.bookmark;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.travellaboratory.be.domain.article.Article;
import site.travellaboratory.be.domain.user.entity.User;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Optional<Bookmark> findByArticleAndUser(final Article article, final User user);

    Optional<List<Bookmark>> findByUserAndStatusIn(final User user, final List<BookmarkStatus> Status);

//    List<Bookmark> findByUserAndStatus(final User user, final BookmarkStatus status);

    Optional<Page<Bookmark>> findByUserAndStatusIn(final User user, final List<BookmarkStatus> Status, Pageable pageable);

    @Query("SELECT COUNT(b) FROM Bookmark b WHERE b.article.id = :articleId AND b.status = :status")
    Long countByArticleIdAndStatus(@Param("articleId") Long articleId, @Param("status") BookmarkStatus status);

    boolean existsByUserIdAndArticleIdAndStatus(Long loginId, Long articleId, BookmarkStatus active);
}
