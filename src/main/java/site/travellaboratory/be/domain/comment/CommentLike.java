package site.travellaboratory.be.domain.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import site.travellaboratory.be.domain.comment.enums.CommentLikeStatus;
import site.travellaboratory.be.infrastructure.domains.user.entity.UserJpaEntity;

@Getter
@Builder
@RequiredArgsConstructor
public class CommentLike {

    private final Long id;
    private final UserJpaEntity userJpaEntity;
    private final Comment comment;
    private final CommentLikeStatus status;

    public static CommentLike create(UserJpaEntity userJpaEntity, Comment comment) {
        return CommentLike.builder()
            .userJpaEntity(userJpaEntity)
            .comment(comment)
            .status(CommentLikeStatus.ACTIVE)
            .build();
    }

    public CommentLike withToggleStatus() {
        return CommentLike.builder()
            .id(this.getId())
            .userJpaEntity(this.getUserJpaEntity())
            .comment(this.getComment())
            .status((this.status == CommentLikeStatus.ACTIVE) ? CommentLikeStatus.INACTIVE : CommentLikeStatus.ACTIVE)
            .build();
    }
}
