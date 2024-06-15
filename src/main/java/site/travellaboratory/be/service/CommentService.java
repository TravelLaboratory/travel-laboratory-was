package site.travellaboratory.be.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.controller.comment.dto.CommentDeleteResponse;
import site.travellaboratory.be.controller.comment.dto.CommentSaveRequest;
import site.travellaboratory.be.controller.comment.dto.CommentSaveResponse;
import site.travellaboratory.be.controller.comment.dto.userlikecomment.CommentToggleLikeResponse;
import site.travellaboratory.be.controller.comment.dto.CommentUpdateRequest;
import site.travellaboratory.be.controller.comment.dto.CommentUpdateResponse;
import site.travellaboratory.be.domain.comment.Comment;
import site.travellaboratory.be.domain.comment.CommentRepository;
import site.travellaboratory.be.domain.comment.CommentStatus;
import site.travellaboratory.be.domain.review.Review;
import site.travellaboratory.be.domain.review.ReviewRepository;
import site.travellaboratory.be.domain.review.ReviewStatus;
import site.travellaboratory.be.domain.user.entity.User;
import site.travellaboratory.be.domain.userlikecomment.UserLikeComment;
import site.travellaboratory.be.domain.userlikecomment.UserLikeCommentRepository;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;
    private final UserLikeCommentRepository userLikeCommentRepository;

    @Transactional
    public CommentSaveResponse saveComment(Long userId, CommentSaveRequest request) {
        // 삭제된 후기에 대한 댓글을 작성할 경우
        Review review = reviewRepository.findByIdAndStatusIn(request.reviewId(),
                List.of(ReviewStatus.ACTIVE, ReviewStatus.PRIVATE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.COMMENT_POST_INVALID,
                HttpStatus.NOT_FOUND));


        // 댓글 작성
        Comment saveComment = commentRepository.save(
            Comment.of(
                review.getUser(),
                review,
                request.replyComment()
            )
        );
        return CommentSaveResponse.from(saveComment.getId());
    }

    @Transactional
    public CommentUpdateResponse updateComment(Long userId, Long commentId, CommentUpdateRequest request) {
        // 삭제된 댓글를 수정할 경우
        Comment comment = commentRepository.findByIdAndStatusIn(commentId, List.of(CommentStatus.ACTIVE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.COMMENT_UPDATE_INVALID,
                HttpStatus.NOT_FOUND));

        // 유저가 작성한 댓글이 아닌 경우
        if (!comment.getUser().getId().equals(userId)) {
            throw new BeApplicationException(ErrorCodes.COMMENT_UPDATE_NOT_USER, HttpStatus.FORBIDDEN);
        }

        // 댓글 업데이트
        comment.update(request.replyComment());
        Comment updateComment = commentRepository.save(comment);
        return CommentUpdateResponse.from(updateComment.getId());
    }

    @Transactional
    public CommentDeleteResponse deleteComment(final Long userId,final Long commentId) {
        // 삭제된 댓글을 삭제할 경우
        Comment comment = commentRepository.findByIdAndStatusIn(commentId, List.of(CommentStatus.ACTIVE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.COMMENT_DELETE_INVALID,
                HttpStatus.NOT_FOUND));

        // 유저가 작성한 댓글이 아닌 경우
        if (!comment.getUser().getId().equals(userId)) {
            throw new BeApplicationException(ErrorCodes.COMMENT_DELETE_NOT_USER, HttpStatus.FORBIDDEN);
        }

        // 댓글 삭제
        comment.delete();
        commentRepository.save(comment);
        return CommentDeleteResponse.from(true);
    }

    @Transactional
    public CommentToggleLikeResponse toggleLikeComment(Long userId, Long commentId) {
        // 삭제된 댓글에 좋아요하려고 할 경우
        Comment comment = commentRepository.findByIdAndStatusIn(commentId, List.of(CommentStatus.ACTIVE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.COMMENT_LIKE_INVALID,
                HttpStatus.NOT_FOUND));

        UserLikeComment userLikeComment = userLikeCommentRepository.findByUserIdAndCommentId(userId, commentId)
            .orElse(null);

        // 댓글에 처음 좋아요를 누른 게 아닌 경우
        if (userLikeComment != null) {
            userLikeComment.toggleStatus();
        } else {
            // 댓글에 처음 좋아요를 누른 경우 - 새로 생성
            User user = User.of(userId);
            userLikeComment = UserLikeComment.of(user, comment);
        }

        UserLikeComment saveLikeComment = userLikeCommentRepository.save(userLikeComment);
        return CommentToggleLikeResponse.from(saveLikeComment.getStatus());
    }
}
