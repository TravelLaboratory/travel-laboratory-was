package site.travellaboratory.be.review.presentation.response.writer;

import site.travellaboratory.be.review.domain.Review;

public record ReviewSaveResponse(
    Long reviewId
) {
    public static ReviewSaveResponse from(Review review) {
        return new ReviewSaveResponse(
            review.getId()
        );
    }
}
