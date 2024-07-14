package site.travellaboratory.be.domain.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.domain.comment.enums.CommentStatus;
import site.travellaboratory.be.domain.review.Review;
import site.travellaboratory.be.domain.user.user.User;

@Getter
@Builder
@RequiredArgsConstructor
public class Comment {

    private final Long id;
    private final User user;
    private final Review review;
    private final String replyContent;
    private final CommentStatus status;

    public static Comment create(User user, Review review, String replyContent) {

        return Comment.builder()
            .user(user)
            .review(review)
            .replyContent(replyContent)
            .status(CommentStatus.ACTIVE)
            .build();
    }

    public Comment withUpdatedReplyContent(User user, String replyContent) {
        // 유저가 작성한 댓글이 아닌 경우
        verifyOwner(user);

        return Comment.builder()
            .id(this.id)
            .user(this.user)
            .review(this.review)
            .replyContent(replyContent)
            .status(this.status)
            .build();
    }

    public Comment withInactiveStatus(User user) {
        // 유저가 작성한 댓글이 아닌 경우
        verifyOwner(user);

        return Comment.builder()
            .id(this.id)
            .user(this.user)
            .review(this.review)
            .replyContent(this.replyContent)
            .status(CommentStatus.INACTIVE)
            .build();
    }

    private void verifyOwner(User user) {
        // 유저가 작성한 댓글이 아닌 경우
        if (!this.getUser().getId().equals(user.getId())) {
            throw new BeApplicationException(ErrorCodes.REVIEW_UPDATE_NOT_USER,
                HttpStatus.FORBIDDEN);
        }
    }
}
