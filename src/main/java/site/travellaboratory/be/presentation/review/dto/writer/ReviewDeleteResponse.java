package site.travellaboratory.be.presentation.review.dto.writer;

public record ReviewDeleteResponse(
    Boolean isDelete
) {
    public static ReviewDeleteResponse from(Boolean isDelete) {
        return new ReviewDeleteResponse(
            isDelete
        );
    }
}

