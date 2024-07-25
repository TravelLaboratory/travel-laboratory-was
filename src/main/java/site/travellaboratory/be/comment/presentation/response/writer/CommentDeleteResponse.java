package site.travellaboratory.be.comment.presentation.response.writer;

import site.travellaboratory.be.comment.domain.Comment;
import site.travellaboratory.be.comment.domain.enums.CommentStatus;

public record CommentDeleteResponse(
    Boolean isDelete
) {
    public static CommentDeleteResponse from(Comment comment) {
        return new CommentDeleteResponse(
            comment.getStatus() == CommentStatus.INACTIVE
        );
    }
}
