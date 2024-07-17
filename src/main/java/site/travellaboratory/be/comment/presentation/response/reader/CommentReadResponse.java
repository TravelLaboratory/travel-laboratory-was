package site.travellaboratory.be.comment.presentation.response.reader;

import java.time.format.DateTimeFormatter;
import site.travellaboratory.be.comment.infrastructure.persistence.entity.CommentEntity;

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
        CommentEntity commentEntity, boolean isEditable, boolean isLike, long likeCount
    ) {
        return new CommentReadResponse(
            commentEntity.getUserEntity().getId(),
            commentEntity.getUserEntity().getProfileImgUrl(),
            commentEntity.getUserEntity().getNickname(),
            isEditable,
            commentEntity.getId(),
            commentEntity.getReplyContent(),
            commentEntity.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            isLike,
            likeCount
        );
    }
}
