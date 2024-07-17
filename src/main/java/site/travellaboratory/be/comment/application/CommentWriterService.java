package site.travellaboratory.be.comment.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.comment.domain.Comment;
import site.travellaboratory.be.comment.domain.enums.CommentStatus;
import site.travellaboratory.be.review.domain.Review;
import site.travellaboratory.be.review.domain.enums.ReviewStatus;
import site.travellaboratory.be.user.domain.enums.UserStatus;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.comment.infrastructure.persistence.entity.CommentEntity;
import site.travellaboratory.be.comment.infrastructure.persistence.repository.CommentJpaRepository;
import site.travellaboratory.be.review.infrastructure.persistence.ReviewJpaRepository;
import site.travellaboratory.be.user.infrastructure.persistence.repository.UserJpaRepository;
import site.travellaboratory.be.comment.presentation.response.writer.CommentSaveRequest;
import site.travellaboratory.be.comment.presentation.response.writer.CommentUpdateRequest;

@Service
@RequiredArgsConstructor
public class CommentWriterService {

    private final ReviewJpaRepository reviewJpaRepository;
    private final CommentJpaRepository commentJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Transactional
    public Long saveComment(Long userId, CommentSaveRequest request) {
        // 유효하지 않은 후기에 대한 댓글을 작성할 경우
        Review review = reviewJpaRepository.findByIdAndStatusIn(request.reviewId(),
                List.of(ReviewStatus.ACTIVE, ReviewStatus.PRIVATE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.COMMENT_POST_INVALID,
                HttpStatus.NOT_FOUND)).toModel();

        // 댓글 쓰는 유저 찾기
        User user = userJpaRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND, HttpStatus.NOT_FOUND)).toModel();

        // 댓글 작성
        Comment saveComment = Comment.create(user, review, request.replyComment());
        CommentEntity savedEntity = commentJpaRepository.save(CommentEntity.from(saveComment));
        return savedEntity.getId();
    }

    @Transactional
    public Long updateComment(Long userId, Long commentId,
        CommentUpdateRequest request) {
        // 유효하지 않은 댓글를 수정할 경우
        Comment comment = commentJpaRepository.findByIdAndStatusIn(commentId,
                List.of(CommentStatus.ACTIVE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.COMMENT_UPDATE_INVALID,
                HttpStatus.NOT_FOUND)).toModel();

        // 댓글을 수정하려는 유저 찾기
        User user = userJpaRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND, HttpStatus.NOT_FOUND)).toModel();

        Comment updateComment = comment.withUpdatedReplyContent(user, request.replyComment());
        CommentEntity savedEntity = commentJpaRepository.save(CommentEntity.from(updateComment));
        return savedEntity.getId();
    }

    @Transactional
    public boolean deleteComment(final Long userId, final Long commentId) {
        // 유효하지 않은 댓글을 삭제할 경우
        Comment comment = commentJpaRepository.findByIdAndStatusIn(commentId,
                List.of(CommentStatus.ACTIVE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.COMMENT_DELETE_INVALID,
                HttpStatus.NOT_FOUND)).toModel();

        User user = userJpaRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND, HttpStatus.NOT_FOUND)).toModel();

        Comment deletedComment = comment.withInactiveStatus(user);

        // 댓글 삭제
        CommentEntity result = commentJpaRepository.save(CommentEntity.from(deletedComment));
        return result.getStatus() == CommentStatus.INACTIVE;
    }
}
