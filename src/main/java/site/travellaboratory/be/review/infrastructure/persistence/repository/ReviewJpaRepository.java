package site.travellaboratory.be.review.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.travellaboratory.be.article.infrastructure.persistence.entity.ArticleEntity;
import site.travellaboratory.be.review.infrastructure.persistence.entity.ReviewEntity;
import site.travellaboratory.be.user.infrastructure.persistence.entity.UserEntity;
import site.travellaboratory.be.review.domain.enums.ReviewStatus;

public interface ReviewJpaRepository extends JpaRepository<ReviewEntity, Long> {

    Optional<ReviewEntity> findByArticleEntityAndStatus(ArticleEntity articleEntity, ReviewStatus status);

    Optional<ReviewEntity> findByIdAndStatus(Long reviewId, ReviewStatus status);

    // 프로필 - 후기 전체 조회
    // FETCH JOIN 으로 N+1 해결
    // todo: before
//    @Query("SELECT t FROM Review t JOIN FETCH t.article a WHERE t.user = :user AND t.status IN :status ORDER BY t.createdAt DESC")
//    Page<Review> findByUserAndStatusInOrderByCreatedAtFetchJoin(@Param("user") User user, @Param("status") List<ReviewStatus> status, Pageable pageable);

    // todo: after(1)
    @Query("SELECT t.id FROM ReviewEntity t WHERE t.userEntity =:user AND t.status =:status ORDER BY t.createdAt DESC")
    Page<Long> findReviewIdsByUserAndStatusOrderByCreatedAt(@Param("user") UserEntity userEntity,
        @Param("status") ReviewStatus status, Pageable pageable);

    // todo: after(2)
    @Query("SELECT t FROM ReviewEntity t JOIN FETCH t.articleEntity a JOIN FETCH a.locationEntities l WHERE t.id IN :ids ORDER BY t.createdAt DESC ")
    List<ReviewEntity> findReviewsWithArticlesAndLocationsByIds(@Param("ids") List<Long> ids);
}
