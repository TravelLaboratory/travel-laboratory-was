package site.travellaboratory.be.review.presentation.response.writer;

public record ReviewUpdateResponse(
    Long reviewId
) {
    public static ReviewUpdateResponse from(Long reviewId) {
        return new ReviewUpdateResponse(
            reviewId
        );
    }
}
