package site.travellaboratory.be.comment.presentation.response.writer;

public record CommentSaveResponse(
    Long commentId
) {
    public static CommentSaveResponse from(Long commentId) {
        return new CommentSaveResponse(commentId);
    }
}
