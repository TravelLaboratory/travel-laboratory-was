package site.travellaboratory.be.comment.presentation.response.reader;

public record CommentLikeCount(
    long commentId,
    long likeCount
) {
    public CommentLikeCount(long commentId, long likeCount) {
        this.commentId = commentId;
        this.likeCount = likeCount;
    }
}
