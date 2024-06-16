package site.travellaboratory.be.controller.comment.dto;

public record CommentLikeCount(
    long commentId,
    long likeCount
) {
    public CommentLikeCount(long commentId, long likeCount) {
        this.commentId = commentId;
        this.likeCount = likeCount;
    }
}
