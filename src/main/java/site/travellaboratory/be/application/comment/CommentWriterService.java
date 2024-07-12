package site.travellaboratory.be.application.comment;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.infrastructure.domains.comment.repository.CommentJpaRepository;
import site.travellaboratory.be.infrastructure.domains.comment.entity.CommentJpaEntity;
import site.travellaboratory.be.domain.comment.enums.CommentStatus;
import site.travellaboratory.be.infrastructure.domains.review.repository.ReviewJpaRepository;
import site.travellaboratory.be.infrastructure.domains.review.entity.ReviewJpaEntity;
import site.travellaboratory.be.domain.review.enums.ReviewStatus;
import site.travellaboratory.be.infrastructure.domains.user.UserRepository;
import site.travellaboratory.be.infrastructure.domains.user.entity.User;
import site.travellaboratory.be.infrastructure.domains.user.enums.UserStatus;
import site.travellaboratory.be.presentation.comment.dto.writer.CommentDeleteResponse;
import site.travellaboratory.be.presentation.comment.dto.writer.CommentSaveRequest;
import site.travellaboratory.be.presentation.comment.dto.writer.CommentSaveResponse;
import site.travellaboratory.be.presentation.comment.dto.writer.CommentUpdateRequest;
import site.travellaboratory.be.presentation.comment.dto.writer.CommentUpdateResponse;

@Service
@RequiredArgsConstructor
public class CommentWriterService {

    private final ReviewJpaRepository reviewJpaRepository;
    private final CommentJpaRepository commentJpaRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentSaveResponse saveComment(Long userId, CommentSaveRequest request) {
        // 유효하지 않은 후기에 대한 댓글을 작성할 경우
        ReviewJpaEntity reviewJpaEntity = reviewJpaRepository.findByIdAndStatusIn(request.reviewId(),
                List.of(ReviewStatus.ACTIVE, ReviewStatus.PRIVATE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.COMMENT_POST_INVALID,
                HttpStatus.NOT_FOUND));

        // 댓글 쓰는 유저 찾기
        User commentUser = userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        // 댓글 작성
        CommentJpaEntity saveCommentJpaEntity = commentJpaRepository.save(
            CommentJpaEntity.of(
                commentUser,
                reviewJpaEntity,
                request.replyComment()
            )
        );
        return CommentSaveResponse.from(saveCommentJpaEntity.getId());
    }

    @Transactional
    public CommentUpdateResponse updateComment(Long userId, Long commentId,
        CommentUpdateRequest request) {
        // 유효하지 않은 댓글를 수정할 경우
        CommentJpaEntity commentJpaEntity = commentJpaRepository.findByIdAndStatusIn(commentId,
                List.of(CommentStatus.ACTIVE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.COMMENT_UPDATE_INVALID,
                HttpStatus.NOT_FOUND));

        // 유저가 작성한 댓글이 아닌 경우
        if (!commentJpaEntity.getUser().getId().equals(userId)) {
            throw new BeApplicationException(ErrorCodes.COMMENT_UPDATE_NOT_USER,
                HttpStatus.FORBIDDEN);
        }

        // 댓글 업데이트
        commentJpaEntity.update(request.replyComment());
        CommentJpaEntity updateCommentJpaEntity = commentJpaRepository.save(commentJpaEntity);
        return CommentUpdateResponse.from(updateCommentJpaEntity.getId());
    }

    @Transactional
    public CommentDeleteResponse deleteComment(final Long userId, final Long commentId) {
        // 유효하지 않은 댓글을 삭제할 경우
        CommentJpaEntity commentJpaEntity = commentJpaRepository.findByIdAndStatusIn(commentId,
                List.of(CommentStatus.ACTIVE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.COMMENT_DELETE_INVALID,
                HttpStatus.NOT_FOUND));

        // 유저가 작성한 댓글이 아닌 경우
        if (!commentJpaEntity.getUser().getId().equals(userId)) {
            throw new BeApplicationException(ErrorCodes.COMMENT_DELETE_NOT_USER,
                HttpStatus.FORBIDDEN);
        }

        // 댓글 삭제
        commentJpaEntity.delete();
        commentJpaRepository.save(commentJpaEntity);
        return CommentDeleteResponse.from(true);
    }
}
