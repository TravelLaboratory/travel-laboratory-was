package site.travellaboratory.be.controller.comment.dto;

import java.time.format.DateTimeFormatter;
import site.travellaboratory.be.domain.comment.Comment;

public record CommentReadResponse(
    Long userId,
    String profileImgUrl,
    String nickname,
    boolean isEditable,
    Long commentId,
    String replyComment,
    String createdAt,
    boolean isLike,
    long likeCount
) {
    public static CommentReadResponse from(
        Comment comment, boolean isEditable, boolean isLike, long likeCount
    ) {
        return new CommentReadResponse(
            comment.getUser().getId(),
            comment.getUser().getProfileImgUrl(),
            comment.getUser().getNickname(),
            isEditable,
            comment.getId(),
            comment.getReplyContent(),
            comment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            isLike,
            likeCount
        );
    }
}
