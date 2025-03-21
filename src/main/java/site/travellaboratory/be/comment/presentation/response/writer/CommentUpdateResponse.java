package site.travellaboratory.be.comment.presentation.response.writer;

import site.travellaboratory.be.comment.domain.Comment;

public record CommentUpdateResponse(
    Long commentId
) {

    public static CommentUpdateResponse from(Comment comment) {
        return new CommentUpdateResponse(
            comment.getId()
        );
    }
}
