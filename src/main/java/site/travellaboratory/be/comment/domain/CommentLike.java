package site.travellaboratory.be.comment.domain;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import site.travellaboratory.be.comment.domain.enums.CommentLikeStatus;
import site.travellaboratory.be.user.domain.User;

@Getter
@Builder
@RequiredArgsConstructor
public class CommentLike {

    private final Long id;
    private final User user;
    private final Comment comment;
    private final CommentLikeStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static CommentLike create(User user, Comment comment) {
        return CommentLike.builder()
            .user(user)
            .comment(comment)
            .status(CommentLikeStatus.ACTIVE)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    public CommentLike withToggleStatus() {
        return CommentLike.builder()
            .id(this.getId())
            .user(this.getUser())
            .comment(this.getComment())
            .status((this.status == CommentLikeStatus.ACTIVE) ? CommentLikeStatus.INACTIVE : CommentLikeStatus.ACTIVE)
            .createdAt(this.createdAt)
            .updatedAt(LocalDateTime.now())
            .build();
    }
}
