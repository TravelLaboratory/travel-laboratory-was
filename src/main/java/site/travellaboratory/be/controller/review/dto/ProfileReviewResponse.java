package site.travellaboratory.be.controller.review.dto;

import java.time.LocalDate;
import java.util.List;
import site.travellaboratory.be.domain.review.Review;

public record ProfileReviewResponse(
    Long reviewId,
    String representativeImgUrl,
    List<ProfileReviewLocation> location,
    LocalDate startAt,
    LocalDate endAt
) {
    public static ProfileReviewResponse from(Review review, List<ProfileReviewLocation> locations) {
        return new ProfileReviewResponse(
            review.getId(),
            review.getRepresentativeImgUrl(),
            locations,
            review.getArticle().getStartAt(),
            review.getArticle().getEndAt());
    }
}


