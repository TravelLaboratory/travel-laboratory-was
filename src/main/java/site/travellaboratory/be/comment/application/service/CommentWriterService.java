package site.travellaboratory.be.comment.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.comment.domain.Comment;
import site.travellaboratory.be.comment.domain.enums.CommentStatus;
import site.travellaboratory.be.comment.domain.request.CommentSaveRequest;
import site.travellaboratory.be.comment.domain.request.CommentUpdateRequest;
import site.travellaboratory.be.comment.infrastructure.persistence.entity.CommentEntity;
import site.travellaboratory.be.comment.infrastructure.persistence.repository.CommentJpaRepository;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.review.application.port.ReviewRepository;
import site.travellaboratory.be.review.domain.Review;
import site.travellaboratory.be.review.domain.enums.ReviewStatus;
import site.travellaboratory.be.user.application.port.UserRepository;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain.enums.UserStatus;

@Service
@RequiredArgsConstructor
public class CommentWriterService {

    private final CommentJpaRepository commentJpaRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long save(Long userId, CommentSaveRequest request) {
        // 유효하지 않은 후기에 대한 댓글을 작성할 경우
        Review review = getReviewById(request.reviewId());
        // 댓글 쓰는 유저 찾기
        User user = getUserById(userId);
        // 댓글 작성
        Comment saveComment = Comment.create(user, review, request);
        CommentEntity savedEntity = commentJpaRepository.save(CommentEntity.from(saveComment));
        return savedEntity.getId();
    }

    @Transactional
    public Long update(Long userId, Long commentId,
        CommentUpdateRequest request) {
        // 유효하지 않은 댓글를 수정할 경우
        Comment comment = commentJpaRepository.findByIdAndStatusIn(commentId,
                List.of(CommentStatus.ACTIVE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.COMMENT_UPDATE_INVALID,
                HttpStatus.NOT_FOUND)).toModel();

        // 댓글을 수정하려는 유저 찾기
        User user = getUserById(userId);

        Comment updateComment = comment.withUpdatedReplyContent(user, request);
        CommentEntity savedEntity = commentJpaRepository.save(CommentEntity.from(updateComment));
        return savedEntity.getId();
    }

    @Transactional
    public boolean delete(final Long userId, final Long commentId) {
        // 유효하지 않은 댓글을 삭제할 경우
        Comment comment = commentJpaRepository.findByIdAndStatusIn(commentId,
                List.of(CommentStatus.ACTIVE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.COMMENT_DELETE_INVALID,
                HttpStatus.NOT_FOUND)).toModel();

        User user = getUserById(userId);

        // 댓글 삭제
        Comment deletedComment = comment.withInactiveStatus(user);
        CommentEntity result = commentJpaRepository.save(CommentEntity.from(deletedComment));
        return result.getStatus() == CommentStatus.INACTIVE;
    }

    private Review getReviewById(Long reviewId) {
        return reviewRepository.findByIdAndStatusIn(reviewId, List.of(ReviewStatus.ACTIVE, ReviewStatus.PRIVATE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.COMMENT_POST_INVALID,
                HttpStatus.NOT_FOUND));
    }

    private User getUserById(Long userId) {
        return userRepository.getByIdAndStatus(userId, UserStatus.ACTIVE);
    }
}
