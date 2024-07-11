package site.travellaboratory.be.presentation.review.dto.like;

import site.travellaboratory.be.infrastructure.domains.userlikereview.enums.UserLikeReviewStatus;

public record ReviewToggleLikeResponse(
    UserLikeReviewStatus status
) {
    public static ReviewToggleLikeResponse from(UserLikeReviewStatus status) {
        return new ReviewToggleLikeResponse(status);
    }
}
