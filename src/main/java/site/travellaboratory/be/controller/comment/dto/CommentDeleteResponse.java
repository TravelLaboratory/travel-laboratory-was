package site.travellaboratory.be.controller.comment.dto;

public record CommentDeleteResponse(
    Boolean isDelete
) {
    public static CommentDeleteResponse from(Boolean isDelete) {
        return new CommentDeleteResponse(
            isDelete
        );
    }
}
