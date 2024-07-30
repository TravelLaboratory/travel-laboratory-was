package site.travellaboratory.be.review.infrastructure.persistence.repository;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.travellaboratory.be.review.application.port.ReviewLikeRepository;
import site.travellaboratory.be.review.domain.ReviewLike;
import site.travellaboratory.be.review.infrastructure.persistence.entity.ReviewLikeEntity;

@Repository
@RequiredArgsConstructor
public class ReviewLikeRepositoryImpl implements ReviewLikeRepository {

    private final ReviewLikeJpaRepository reviewLikeJpaRepository;

    @Override
    public Optional<ReviewLike> findByUserIdAndReviewId(Long userId, Long reviewJpaEntityId) {
        return reviewLikeJpaRepository.findByUserIdAndReviewId(userId, reviewJpaEntityId).map(ReviewLikeEntity::toModel);
    }

    @Override
    public ReviewLike save(ReviewLike reviewLike) {
        return reviewLikeJpaRepository.save(ReviewLikeEntity.from(reviewLike)).toModel();
    }
}
