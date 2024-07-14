package site.travellaboratory.be.presentation.review.dto.reader;

import java.time.LocalDate;
import java.util.List;
import site.travellaboratory.be.infrastructure.domains.review.entity.ReviewJpaEntity;

public record ProfileReviewResponse(
    Long reviewId,
    String title,
    String representativeImgUrl,
    List<ProfileReviewLocation> location,
    LocalDate startAt,
    LocalDate endAt
) {
    public static ProfileReviewResponse from(ReviewJpaEntity reviewJpaEntity, List<ProfileReviewLocation> locations) {
        return new ProfileReviewResponse(
            reviewJpaEntity.getId(),
            reviewJpaEntity.getTitle(),
            reviewJpaEntity.getRepresentativeImgUrl(),
            locations,
            reviewJpaEntity.getArticleJpaEntity().getStartAt(),
            reviewJpaEntity.getArticleJpaEntity().getEndAt());
    }
}


