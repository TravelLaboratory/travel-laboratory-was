package site.travellaboratory.be.comment.presentation.response.writer;

public record CommentDeleteResponse(
    Boolean isDelete
) {
    public static CommentDeleteResponse from(Boolean isDelete) {
        return new CommentDeleteResponse(
            isDelete
        );
    }
}
