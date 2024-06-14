package site.travellaboratory.be.domain.article;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.travellaboratory.be.domain.user.entity.User;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    default Article getById(final Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시물은 없습니다."));
    }

    List<Article> findArticlesByUser(final User user);

    Optional<Article> findByIdAndStatusIn(final Long articleId, List<ArticleStatus> Status);
}
