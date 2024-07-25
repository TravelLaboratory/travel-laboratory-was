package site.travellaboratory.be.review.presentation.response.writer;

import site.travellaboratory.be.review.domain.Review;
import site.travellaboratory.be.review.domain.enums.ReviewStatus;

public record ReviewUpdateResponse(
    Long reviewId,
    String title,
    String representativeImgUrl,
    String description,
    ReviewStatus status
) {
    public static ReviewUpdateResponse from(Review review) {
        return new ReviewUpdateResponse(
            review.getId(),
            review.getTitle(),
            review.getRepresentativeImgUrl(),
            review.getDescription(),
            review.getStatus()
        );
    }
}
