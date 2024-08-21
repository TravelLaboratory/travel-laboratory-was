package site.travellaboratory.be.comment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.comment.application.port.CommentRepository;
import site.travellaboratory.be.comment.domain.Comment;
import site.travellaboratory.be.comment.domain.enums.CommentStatus;
import site.travellaboratory.be.comment.domain.request.CommentSaveRequest;
import site.travellaboratory.be.comment.domain.request.CommentUpdateRequest;
import site.travellaboratory.be.common.error.ErrorCodes;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.review.application.port.ReviewRepository;
import site.travellaboratory.be.review.domain.Review;
import site.travellaboratory.be.review.domain.enums.ReviewStatus;
import site.travellaboratory.be.user.application.port.UserRepository;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain.enums.UserStatus;

@Service
@RequiredArgsConstructor
public class CommentWriterService {

    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Transactional
    public Comment save(Long userId, CommentSaveRequest request) {
        Review review = getReviewById(request.reviewId());
        User user = getUserById(userId);

        Comment saveComment = Comment.create(user, review, request);
        return commentRepository.save(saveComment);
    }

    @Transactional
    public Comment update(Long userId, Long commentId, CommentUpdateRequest request) {
        Comment comment = getCommentById(commentId);
        User user = getUserById(userId);

        Comment updateComment = comment.withUpdatedReplyContent(user, request);
        return commentRepository.save(updateComment);
    }

    @Transactional
    public Comment delete(final Long userId, final Long commentId) {
        Comment comment = getCommentById(commentId);
        User user = getUserById(userId);

        Comment deletedComment = comment.withInactiveStatus(user);
        return commentRepository.save(deletedComment);
    }

    private Review getReviewById(Long reviewId) {
        return reviewRepository.findByIdAndStatus(reviewId, ReviewStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.COMMENT_INVALID_REVIEW_ID,
                HttpStatus.NOT_FOUND));
    }

    private User getUserById(Long userId) {
        return userRepository.getByIdAndStatus(userId, UserStatus.ACTIVE);
    }

    private Comment getCommentById(Long commentId) {
        return commentRepository.findByIdAndStatus(commentId, CommentStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.COMMENT_INVALID_COMMENT_ID,
                HttpStatus.NOT_FOUND));
    }
}
