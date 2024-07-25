package site.travellaboratory.be.comment.presentation.response.writer;

import site.travellaboratory.be.comment.domain.Comment;

public record CommentSaveResponse(
    Long commentId
) {
    public static CommentSaveResponse from(Comment comment) {
        return new CommentSaveResponse(comment.getId());
    }
}
