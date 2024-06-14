package site.travellaboratory.be.controller.review.dto;

public record ReviewUpdateResponse(
    Long reviewId
) {
    public static ReviewUpdateResponse from(Long reviewId) {
        return new ReviewUpdateResponse(
            reviewId
        );
    }
}
