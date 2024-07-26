package site.travellaboratory.be.review.presentation.response.writer;

import site.travellaboratory.be.review.domain.Review;

public record ReviewUpdateResponse(
    Long reviewId
) {
    public static ReviewUpdateResponse from(Review review) {
        return new ReviewUpdateResponse(
            review.getId()
        );
    }
}
