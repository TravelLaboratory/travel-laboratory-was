package site.travellaboratory.be.presentation.comment.dto.writer;

public record CommentUpdateResponse(
    Long commentId
) {

    public static CommentUpdateResponse from(Long commentId) {
        return new CommentUpdateResponse(commentId);
    }
}