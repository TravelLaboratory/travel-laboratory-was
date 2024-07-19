package site.travellaboratory.be.review.presentation.response.writer;

public record ReviewDeleteResponse(
    Boolean isDelete
) {
    public static ReviewDeleteResponse from(Boolean isDelete) {
        return new ReviewDeleteResponse(
            isDelete
        );
    }
}

