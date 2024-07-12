package site.travellaboratory.be.infrastructure.domains.userlikereview;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.travellaboratory.be.infrastructure.domains.userlikereview.entity.ReviewLikeJpaEntity;
import site.travellaboratory.be.domain.review.enums.ReviewLikeStatus;

public interface ReviewLikeRepository extends JpaRepository<ReviewLikeJpaEntity, Long> {

    @Query("SELECT t FROM ReviewLikeJpaEntity t WHERE t.user.id = :userId AND t.reviewJpaEntity.id = :reviewJpaEntityId")
    Optional<ReviewLikeJpaEntity> findByUserIdAndReviewId(@Param("userId") Long userId, @Param("reviewJpaEntityId") Long reviewJpaEntityId);

    // 후기 상세 조회 - 좋아요 개수
    Long countByReviewJpaEntityIdAndStatus(Long reviewId, ReviewLikeStatus status);
}
