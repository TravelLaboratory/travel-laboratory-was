package site.travellaboratory.be.domain.bookmark;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import site.travellaboratory.be.domain.user.User;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findBookmarkByUser(final User user);
}
