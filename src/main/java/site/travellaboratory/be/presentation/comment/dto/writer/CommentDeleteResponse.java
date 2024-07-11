package site.travellaboratory.be.presentation.comment.dto.writer;

public record CommentDeleteResponse(
    Boolean isDelete
) {
    public static CommentDeleteResponse from(Boolean isDelete) {
        return new CommentDeleteResponse(
            isDelete
        );
    }
}
