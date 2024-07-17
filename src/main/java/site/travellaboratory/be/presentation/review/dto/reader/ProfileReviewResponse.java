package site.travellaboratory.be.presentation.review.dto.reader;

import java.time.LocalDate;
import java.util.List;
import site.travellaboratory.be.infrastructure.domains.review.entity.ReviewEntity;

public record ProfileReviewResponse(
    Long reviewId,
    String title,
    String representativeImgUrl,
    List<ProfileReviewLocation> location,
    LocalDate startAt,
    LocalDate endAt
) {
    public static ProfileReviewResponse from(ReviewEntity reviewEntity, List<ProfileReviewLocation> locations) {
        return new ProfileReviewResponse(
            reviewEntity.getId(),
            reviewEntity.getTitle(),
            reviewEntity.getRepresentativeImgUrl(),
            locations,
            reviewEntity.getArticleEntity().getStartAt(),
            reviewEntity.getArticleEntity().getEndAt());
    }
}


