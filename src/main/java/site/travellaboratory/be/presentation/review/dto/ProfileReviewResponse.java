package site.travellaboratory.be.presentation.review.dto;

import java.time.LocalDate;
import java.util.List;
import site.travellaboratory.be.infrastructure.review.entity.Review;

public record ProfileReviewResponse(
    Long reviewId,
    String title,
    String representativeImgUrl,
    List<ProfileReviewLocation> location,
    LocalDate startAt,
    LocalDate endAt
) {
    public static ProfileReviewResponse from(Review review, List<ProfileReviewLocation> locations) {
        return new ProfileReviewResponse(
            review.getId(),
            review.getTitle(),
            review.getRepresentativeImgUrl(),
            locations,
            review.getArticle().getStartAt(),
            review.getArticle().getEndAt());
    }
}


