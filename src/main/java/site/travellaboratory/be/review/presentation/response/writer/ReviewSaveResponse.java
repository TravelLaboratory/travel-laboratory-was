package site.travellaboratory.be.review.presentation.response.writer;

public record ReviewSaveResponse(
    Long reviewId
) {
    public static ReviewSaveResponse from(Long reviewId) {
        return new ReviewSaveResponse(
            reviewId
        );
    }
}
