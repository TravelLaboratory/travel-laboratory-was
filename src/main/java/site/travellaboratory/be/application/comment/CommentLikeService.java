package site.travellaboratory.be.application.comment;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.domain.comment.Comment;
import site.travellaboratory.be.domain.comment.CommentLike;
import site.travellaboratory.be.domain.comment.enums.CommentLikeStatus;
import site.travellaboratory.be.infrastructure.domains.comment.repository.CommentJpaRepository;
import site.travellaboratory.be.domain.comment.enums.CommentStatus;
import site.travellaboratory.be.infrastructure.domains.user.UserJpaRepository;
import site.travellaboratory.be.infrastructure.domains.user.entity.UserJpaEntity;
import site.travellaboratory.be.infrastructure.domains.comment.repository.CommentLikeJpaRepository;
import site.travellaboratory.be.infrastructure.domains.comment.entity.CommentLikeJpaEntity;
import site.travellaboratory.be.domain.user.enums.UserStatus;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentJpaRepository commentJpaRepository;
    private final CommentLikeJpaRepository commentLikeJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Transactional
    public CommentLikeStatus toggleLikeComment(Long userId, Long commentId) {
        // 유효하지 않은 댓글에 좋아요하려고 할 경우
        Comment comment = commentJpaRepository.findByIdAndStatusIn(commentId,
                List.of(CommentStatus.ACTIVE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.COMMENT_LIKE_INVALID,
                HttpStatus.NOT_FOUND)).toModel();

        CommentLikeJpaEntity commentLikeJpaEntity = commentLikeJpaRepository.findByUserIdAndCommentId(userId,
                commentId)
            .orElse(null);

        // 좋아요 누른 유저 가져오기
        UserJpaEntity userJpaEntity = userJpaRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        CommentLike commentLike;
        if (commentLikeJpaEntity != null) {
            commentLike = commentLikeJpaEntity.toModel().withToggleStatus();
        } else {
            commentLike = CommentLike.create(userJpaEntity, comment);
        }
        CommentLikeJpaEntity saveCommentLike = commentLikeJpaRepository.save(CommentLikeJpaEntity.from(commentLike));
        return saveCommentLike.getStatus();
    }
}
