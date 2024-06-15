package site.travellaboratory.be.controller.comment.dto.userlikecomment;

import site.travellaboratory.be.domain.userlikecomment.UserLikeCommentStatus;

public record CommentToggleLikeResponse(
    UserLikeCommentStatus status
) {
    public static CommentToggleLikeResponse from(UserLikeCommentStatus status) {
        return new CommentToggleLikeResponse(status);
    }
}
