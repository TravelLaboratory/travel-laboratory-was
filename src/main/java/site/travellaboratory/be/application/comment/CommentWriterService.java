package site.travellaboratory.be.application.comment;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.infrastructure.comment.CommentRepository;
import site.travellaboratory.be.infrastructure.comment.entity.Comment;
import site.travellaboratory.be.infrastructure.comment.enums.CommentStatus;
import site.travellaboratory.be.infrastructure.review.ReviewRepository;
import site.travellaboratory.be.infrastructure.review.entity.Review;
import site.travellaboratory.be.infrastructure.review.enums.ReviewStatus;
import site.travellaboratory.be.infrastructure.user.UserRepository;
import site.travellaboratory.be.infrastructure.user.entity.User;
import site.travellaboratory.be.infrastructure.user.enums.UserStatus;
import site.travellaboratory.be.presentation.comment.dto.writer.CommentDeleteResponse;
import site.travellaboratory.be.presentation.comment.dto.writer.CommentSaveRequest;
import site.travellaboratory.be.presentation.comment.dto.writer.CommentSaveResponse;
import site.travellaboratory.be.presentation.comment.dto.writer.CommentUpdateRequest;
import site.travellaboratory.be.presentation.comment.dto.writer.CommentUpdateResponse;

@Service
@RequiredArgsConstructor
public class CommentWriterService {

    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentSaveResponse saveComment(Long userId, CommentSaveRequest request) {
        // 유효하지 않은 후기에 대한 댓글을 작성할 경우
        Review review = reviewRepository.findByIdAndStatusIn(request.reviewId(),
                List.of(ReviewStatus.ACTIVE, ReviewStatus.PRIVATE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.COMMENT_POST_INVALID,
                HttpStatus.NOT_FOUND));

        // 댓글 쓰는 유저 찾기
        User commentUser = userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        // 댓글 작성
        Comment saveComment = commentRepository.save(
            Comment.of(
                commentUser,
                review,
                request.replyComment()
            )
        );
        return CommentSaveResponse.from(saveComment.getId());
    }

    @Transactional
    public CommentUpdateResponse updateComment(Long userId, Long commentId,
        CommentUpdateRequest request) {
        // 유효하지 않은 댓글를 수정할 경우
        Comment comment = commentRepository.findByIdAndStatusIn(commentId,
                List.of(CommentStatus.ACTIVE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.COMMENT_UPDATE_INVALID,
                HttpStatus.NOT_FOUND));

        // 유저가 작성한 댓글이 아닌 경우
        if (!comment.getUser().getId().equals(userId)) {
            throw new BeApplicationException(ErrorCodes.COMMENT_UPDATE_NOT_USER,
                HttpStatus.FORBIDDEN);
        }

        // 댓글 업데이트
        comment.update(request.replyComment());
        Comment updateComment = commentRepository.save(comment);
        return CommentUpdateResponse.from(updateComment.getId());
    }

    @Transactional
    public CommentDeleteResponse deleteComment(final Long userId, final Long commentId) {
        // 유효하지 않은 댓글을 삭제할 경우
        Comment comment = commentRepository.findByIdAndStatusIn(commentId,
                List.of(CommentStatus.ACTIVE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.COMMENT_DELETE_INVALID,
                HttpStatus.NOT_FOUND));

        // 유저가 작성한 댓글이 아닌 경우
        if (!comment.getUser().getId().equals(userId)) {
            throw new BeApplicationException(ErrorCodes.COMMENT_DELETE_NOT_USER,
                HttpStatus.FORBIDDEN);
        }

        // 댓글 삭제
        comment.delete();
        commentRepository.save(comment);
        return CommentDeleteResponse.from(true);
    }
}
