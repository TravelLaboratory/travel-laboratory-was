package site.travellaboratory.be.comment.presentation.response.like;

import site.travellaboratory.be.comment.domain.CommentLike;
import site.travellaboratory.be.comment.domain.enums.CommentLikeStatus;

public record CommentToggleLikeResponse(
    CommentLikeStatus status
) {
    public static CommentToggleLikeResponse from(CommentLike commentLike) {
        return new CommentToggleLikeResponse(commentLike.getStatus());
    }
}
