package site.travellaboratory.be.comment.presentation.response.writer;

public record CommentUpdateResponse(
    Long commentId
) {

    public static CommentUpdateResponse from(Long commentId) {
        return new CommentUpdateResponse(commentId);
    }
}
