package site.travellaboratory.be.domain.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.domain.comment.enums.CommentStatus;
import site.travellaboratory.be.domain.review.Review;
import site.travellaboratory.be.infrastructure.domains.user.entity.UserJpaEntity;

@Getter
@Builder
@RequiredArgsConstructor
public class Comment {

    private final Long id;
    private final UserJpaEntity userJpaEntity;
    private final Review review;
    private final String replyContent;
    private final CommentStatus status;

    public static Comment create(UserJpaEntity userJpaEntity, Review review, String replyContent) {

        return Comment.builder()
            .userJpaEntity(userJpaEntity)
            .review(review)
            .replyContent(replyContent)
            .status(CommentStatus.ACTIVE)
            .build();
    }

    public Comment withUpdatedReplyContent(UserJpaEntity userJpaEntity, String replyContent) {
        // 유저가 작성한 댓글이 아닌 경우
        verifyOwner(userJpaEntity);

        return Comment.builder()
            .id(this.id)
            .userJpaEntity(this.userJpaEntity)
            .review(this.review)
            .replyContent(replyContent)
            .status(this.status)
            .build();
    }

    public Comment withInactiveStatus(UserJpaEntity userJpaEntity) {
        // 유저가 작성한 댓글이 아닌 경우
        verifyOwner(userJpaEntity);

        return Comment.builder()
            .id(this.id)
            .userJpaEntity(this.userJpaEntity)
            .review(this.review)
            .replyContent(this.replyContent)
            .status(CommentStatus.INACTIVE)
            .build();
    }

    private void verifyOwner(UserJpaEntity userJpaEntity) {
        // 유저가 작성한 댓글이 아닌 경우
        if (!this.getUserJpaEntity().getId().equals(userJpaEntity.getId())) {
            throw new BeApplicationException(ErrorCodes.REVIEW_UPDATE_NOT_USER,
                HttpStatus.FORBIDDEN);
        }
    }
}
