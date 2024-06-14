package site.travellaboratory.be.controller.review.dto;

public record ReviewSaveResponse(
    Long reviewId
) {
    public static ReviewSaveResponse from(Long reviewId) {
        return new ReviewSaveResponse(
            reviewId
        );
    }
}
