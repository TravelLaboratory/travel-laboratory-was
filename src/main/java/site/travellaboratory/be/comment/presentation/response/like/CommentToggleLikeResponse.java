package site.travellaboratory.be.comment.presentation.response.like;

import site.travellaboratory.be.comment.domain.enums.CommentLikeStatus;

public record CommentToggleLikeResponse(
    CommentLikeStatus status
) {
    public static CommentToggleLikeResponse from(CommentLikeStatus status) {
        return new CommentToggleLikeResponse(status);
    }
}
