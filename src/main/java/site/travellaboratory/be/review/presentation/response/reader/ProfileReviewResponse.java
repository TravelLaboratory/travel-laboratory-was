package site.travellaboratory.be.review.presentation.response.reader;

import java.time.LocalDate;
import java.util.List;
import site.travellaboratory.be.review.infrastructure.persistence.ReviewEntity;

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


