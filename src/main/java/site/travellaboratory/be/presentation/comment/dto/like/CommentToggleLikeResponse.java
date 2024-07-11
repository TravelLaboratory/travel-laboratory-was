package site.travellaboratory.be.presentation.comment.dto.like;

import site.travellaboratory.be.infrastructure.domains.userlikecomment.enums.UserLikeCommentStatus;

public record CommentToggleLikeResponse(
    UserLikeCommentStatus status
) {
    public static CommentToggleLikeResponse from(UserLikeCommentStatus status) {
        return new CommentToggleLikeResponse(status);
    }
}
