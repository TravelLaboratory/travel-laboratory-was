package site.travellaboratory.be.comment.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import site.travellaboratory.be.comment.domain.enums.CommentStatus;
import site.travellaboratory.be.comment.domain.request.CommentSaveRequest;
import site.travellaboratory.be.comment.domain.request.CommentUpdateRequest;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.review.domain.Review;
import site.travellaboratory.be.user.domain.User;

@Getter
@Builder
@RequiredArgsConstructor
public class Comment {

    private final Long id;
    private final User user;
    private final Review review;
    private final String replyComment;
    private final CommentStatus status;

    public static Comment create(User user, Review review, CommentSaveRequest saveRequest) {
        return Comment.builder()
            .user(user)
            .review(review)
            .replyComment(saveRequest.replyComment())
            .status(CommentStatus.ACTIVE)
            .build();
    }

    public Comment withUpdatedReplyContent(User user, CommentUpdateRequest updateRequest) {
        // 유저가 작성한 댓글이 아닌 경우
        verifyOwner(user);
        return Comment.builder()
            .id(this.id)
            .user(this.user)
            .review(this.review)
            .replyComment(updateRequest.replyComment())
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
            .replyComment(this.replyComment)
            .status(CommentStatus.INACTIVE)
            .build();
    }

    private void verifyOwner(User user) {
        // 유저가 작성한 댓글이 아닌 경우
        if (!this.getUser().getId().equals(user.getId())) {
            throw new BeApplicationException(ErrorCodes.COMMENT_VERIFY_OWNER,
                HttpStatus.FORBIDDEN);
        }
    }
}
