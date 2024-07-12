package site.travellaboratory.be.presentation.review.dto.like;

import site.travellaboratory.be.domain.review.enums.ReviewLikeStatus;

public record ReviewToggleLikeResponse(
    ReviewLikeStatus status
) {
    public static ReviewToggleLikeResponse from(ReviewLikeStatus status) {
        return new ReviewToggleLikeResponse(status);
    }
}
