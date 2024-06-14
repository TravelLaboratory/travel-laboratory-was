package site.travellaboratory.be.controller.review.dto;

public record ReviewDeleteResponse(
    Boolean isDelete
) {
    public static ReviewDeleteResponse from(Boolean isDelete) {
        return new ReviewDeleteResponse(
            isDelete
        );
    }
}

