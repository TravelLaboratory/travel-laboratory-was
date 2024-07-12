package site.travellaboratory.be.domain.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import site.travellaboratory.be.domain.comment.enums.CommentLikeStatus;
import site.travellaboratory.be.infrastructure.domains.user.entity.User;

@Getter
@Builder
@RequiredArgsConstructor
public class CommentLike {

    private final Long id;
    private final User user;
    private final Comment comment;
    private final CommentLikeStatus status;

    public static CommentLike create(User user, Comment comment) {
        return CommentLike.builder()
            .user(user)
            .comment(comment)
            .status(CommentLikeStatus.ACTIVE)
            .build();
    }

    public CommentLike withToggleStatus() {
        return CommentLike.builder()
            .id(this.getId())
            .user(this.getUser())
            .comment(this.getComment())
            .status((this.status == CommentLikeStatus.ACTIVE) ? CommentLikeStatus.INACTIVE : CommentLikeStatus.ACTIVE)
            .build();
    }
}
