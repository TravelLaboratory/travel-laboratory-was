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

    Optional<Review> findByArticleAndStatusInOrderByArticleDesc(Article article,
        List<ReviewStatus> status);

    Optional<Review> findByIdAndStatusIn(Long reviewId, List<ReviewStatus> status);

    // 일정 상세에서 reviewId 찾아오기
    Optional<Review> findByArticleAndStatus(Article article, ReviewStatus status);

    // 프로필 - 후기 전체 조회
    // FETCH JOIN 으로 N+1 해결
    // todo: before
//    @Query("SELECT t FROM Review t JOIN FETCH t.article a WHERE t.user = :user AND t.status IN :status ORDER BY t.createdAt DESC")
//    Page<Review> findByUserAndStatusInOrderByCreatedAtFetchJoin(@Param("user") User user, @Param("status") List<ReviewStatus> status, Pageable pageable);

    // todo: after(1)
    @Query("SELECT t.id FROM Review t WHERE t.user = :user AND t.status IN :status ORDER BY t.createdAt DESC")
    Page<Long> findReviewIdsByUserAndStatusInOrderByCreatedAt(@Param("user") User user,
        @Param("status") List<ReviewStatus> status, Pageable pageable);

    // todo: after(2)
    @Query("SELECT t FROM Review t JOIN FETCH t.article a JOIN FETCH a.location l WHERE t.id IN :ids ORDER BY t.createdAt DESC ")
    List<Review> findReviewsWithArticlesAndLocationsByIds(@Param("ids") List<Long> ids);


    // 홈(배너) 최신 여행 후기 - 조회 리스트 8개 [feat. 비회원, 회원 공통 항상]
    @Query("SELECT t.id FROM Review t WHERE t.status = 'ACTIVE' ORDER BY t.createdAt DESC")
    Page<Long> findReviewIdsByStatusOrderByCreatedAt(Pageable pageable);

    @Query("SELECT t FROM Review t JOIN FETCH t.article a JOIN FETCH a.location l JOIN FETCH t.user u WHERE t.id IN :ids ORDER BY t.createdAt DESC")
    List<Review> findReviewsWithArticlesAndLocationsByIdsAndUserStatus(@Param("ids") List<Long> ids);
}
