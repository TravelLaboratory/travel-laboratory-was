package site.travellaboratory.be.presentation.comment.dto.reader;

import java.time.format.DateTimeFormatter;
import site.travellaboratory.be.infrastructure.domains.comment.entity.CommentJpaEntity;

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
        CommentJpaEntity commentJpaEntity, boolean isEditable, boolean isLike, long likeCount
    ) {
        return new CommentReadResponse(
            commentJpaEntity.getUser().getId(),
            commentJpaEntity.getUser().getProfileImgUrl(),
            commentJpaEntity.getUser().getNickname(),
            isEditable,
            commentJpaEntity.getId(),
            commentJpaEntity.getReplyContent(),
            commentJpaEntity.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            isLike,
            likeCount
        );
    }
}
