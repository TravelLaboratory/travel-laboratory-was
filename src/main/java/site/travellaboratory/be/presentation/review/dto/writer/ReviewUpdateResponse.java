package site.travellaboratory.be.presentation.review.dto.writer;

public record ReviewUpdateResponse(
    Long reviewId
) {
    public static ReviewUpdateResponse from(Long reviewId) {
        return new ReviewUpdateResponse(
            reviewId
        );
    }
}