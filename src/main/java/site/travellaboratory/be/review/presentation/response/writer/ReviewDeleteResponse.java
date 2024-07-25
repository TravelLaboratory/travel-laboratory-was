package site.travellaboratory.be.review.presentation.response.writer;

import site.travellaboratory.be.review.domain.Review;
import site.travellaboratory.be.review.domain.enums.ReviewStatus;

public record ReviewDeleteResponse(
    Boolean isDelete
) {
    public static ReviewDeleteResponse from(Review review) {
        return new ReviewDeleteResponse(
            review.getStatus() == ReviewStatus.INACTIVE
        );
    }
}

