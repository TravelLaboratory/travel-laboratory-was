package site.travellaboratory.be.controller.review.dto.userlikereview;

import site.travellaboratory.be.domain.userlikereview.UserLikeReviewStatus;

public record ReviewToggleLikeResponse(
    UserLikeReviewStatus status
) {
    public static ReviewToggleLikeResponse from(UserLikeReviewStatus status) {
        return new ReviewToggleLikeResponse(status);
    }
}
