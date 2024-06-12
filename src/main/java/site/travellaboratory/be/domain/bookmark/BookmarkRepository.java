package site.travellaboratory.be.domain.bookmark;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import site.travellaboratory.be.domain.article.Article;
import site.travellaboratory.be.domain.user.entity.User;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Bookmark findByArticleAndUser(final Article article, final User user);

    List<Bookmark> findBookmarkByUser(final User user);
}
