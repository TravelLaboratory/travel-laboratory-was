package site.travellaboratory.be.presentation.comment.dto.userlikecomment;

import site.travellaboratory.be.infrastructure.userlikecomment.enums.UserLikeCommentStatus;

public record CommentToggleLikeResponse(
    UserLikeCommentStatus status
) {
    public static CommentToggleLikeResponse from(UserLikeCommentStatus status) {
        return new CommentToggleLikeResponse(status);
    }
}
