package site.travellaboratory.be.domain.userlikereview;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserLikeReviewRepository extends JpaRepository<UserLikeReview, Long> {

    @Query("SELECT t FROM UserLikeReview t WHERE t.user.id = :userId AND t.review.id = :reviewId")
    Optional<UserLikeReview> findByUserIdAndReviewId(@Param("userId") Long userId, @Param("reviewId") Long reviewId);

    // 후기 상세 조회 - 좋아요 개수
    Long countByReviewIdAndStatus(Long reviewId, UserLikeReviewStatus status);
}
