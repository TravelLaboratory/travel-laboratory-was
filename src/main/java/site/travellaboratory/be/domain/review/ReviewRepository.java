package site.travellaboratory.be.domain.review;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.travellaboratory.be.domain.article.Article;
import site.travellaboratory.be.domain.user.entity.User;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByArticleAndStatusInOrderByArticleDesc(Article article, List<ReviewStatus> status);

    Optional<Review> findByIdAndStatusIn(Long reviewId, List<ReviewStatus> status);

    // 일정 상세에서 reviewId 찾아오기
    Optional<Review> findByArticleAndStatus(Article article, ReviewStatus status);

    // 프로필 - 후기 전체 조회
    // FETCH JOIN 으로 N+! 해결
    @Query("SELECT t FROM Review t JOIN FETCH t.article a WHERE t.user = :user AND t.status IN :status ORDER BY t.createdAt DESC")
    Page<Review> findByUserAndStatusInOrderByCreatedAtFetchJoin(@Param("user") User user, @Param("status") List<ReviewStatus> status, Pageable pageable);
}
