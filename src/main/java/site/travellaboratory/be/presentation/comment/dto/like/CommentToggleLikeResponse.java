package site.travellaboratory.be.presentation.comment.dto.like;

import site.travellaboratory.be.domain.comment.enums.CommentLikeStatus;

public record CommentToggleLikeResponse(
    CommentLikeStatus status
) {
    public static CommentToggleLikeResponse from(CommentLikeStatus status) {
        return new CommentToggleLikeResponse(status);
    }
}
