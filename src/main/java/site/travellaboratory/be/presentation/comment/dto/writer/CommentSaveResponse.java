package site.travellaboratory.be.presentation.comment.dto.writer;

public record CommentSaveResponse(
    Long commentId
) {
    public static CommentSaveResponse from(Long commentId) {
        return new CommentSaveResponse(commentId);
    }
}