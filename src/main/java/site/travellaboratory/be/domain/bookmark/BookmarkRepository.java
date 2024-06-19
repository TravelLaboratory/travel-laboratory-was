package site.travellaboratory.be.domain.bookmark;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import site.travellaboratory.be.domain.article.Article;
import site.travellaboratory.be.domain.user.entity.User;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Optional<Bookmark> findByArticleAndUser(final Article article, final User user);

    List<Bookmark> findByUserAndStatus(final User user, final BookmarkStatus status);

    Optional<List<Bookmark>> findByUserAndStatusIn(final User user, final List<BookmarkStatus> Status);
}
