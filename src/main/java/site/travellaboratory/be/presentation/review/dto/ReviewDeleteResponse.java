package site.travellaboratory.be.presentation.review.dto;

public record ReviewDeleteResponse(
    Boolean isDelete
) {
    public static ReviewDeleteResponse from(Boolean isDelete) {
        return new ReviewDeleteResponse(
            isDelete
        );
    }
}

