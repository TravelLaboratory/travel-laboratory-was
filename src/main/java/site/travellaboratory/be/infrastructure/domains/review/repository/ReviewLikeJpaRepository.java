package site.travellaboratory.be.infrastructure.domains.review.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.travellaboratory.be.infrastructure.domains.review.entity.ReviewLikeEntity;
import site.travellaboratory.be.domain.review.enums.ReviewLikeStatus;

public interface ReviewLikeJpaRepository extends JpaRepository<ReviewLikeEntity, Long> {

    @Query("SELECT t FROM ReviewLikeEntity t WHERE t.userEntity.id = :userId AND t.reviewEntity.id = :reviewJpaEntityId")
    Optional<ReviewLikeEntity> findByUserIdAndReviewId(@Param("userId") Long userId, @Param("reviewJpaEntityId") Long reviewJpaEntityId);

    // 후기 상세 조회 - 좋아요 개수
    Long countByReviewEntityIdAndStatus(Long reviewId, ReviewLikeStatus status);
}
