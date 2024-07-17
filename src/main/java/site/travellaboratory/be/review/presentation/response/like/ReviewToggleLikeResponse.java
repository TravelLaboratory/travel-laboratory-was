package site.travellaboratory.be.review.presentation.response.like;

import site.travellaboratory.be.review.domain.enums.ReviewLikeStatus;

public record ReviewToggleLikeResponse(
    ReviewLikeStatus status
) {
    public static ReviewToggleLikeResponse from(ReviewLikeStatus status) {
        return new ReviewToggleLikeResponse(status);
    }
}
