package site.travellaboratory.be.domain.bookmark;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.travellaboratory.be.domain.article.Article;
import site.travellaboratory.be.domain.user.entity.User;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Optional<Bookmark> findByArticleAndUser(final Article article, final User user);

    List<Bookmark> findByUserAndStatus(final User user, final BookmarkStatus status);

    Optional<List<Bookmark>> findByUserAndStatusIn(final User user, final List<BookmarkStatus> Status);

    @Query("SELECT COUNT(b) FROM Bookmark b WHERE b.article.id = :articleId AND b.status = :status")
    Long countByArticleIdAndStatus(@Param("articleId") Long articleId, @Param("status") BookmarkStatus status);

    boolean existsByUserIdAndArticleIdAndStatus(Long loginId, Long articleId, BookmarkStatus active);

//    @Query("SELECT t.comment.id, COUNT(t) FROM UserLikeComment t WHERE t.comment.id IN :commentIds AND t.status = :status GROUP BY t.comment.id")
//    List<Object[]> countByCommentIdsAndStatusGroupByCommentId(@Param("commentIds") List<Long> commentIds, @Param("status") UserLikeCommentStatus status);
}
