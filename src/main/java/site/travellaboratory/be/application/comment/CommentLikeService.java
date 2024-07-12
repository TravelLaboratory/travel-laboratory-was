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
import site.travellaboratory.be.infrastructure.domains.user.entity.User;
import site.travellaboratory.be.infrastructure.domains.comment.repository.CommentLikeJpaRepository;
import site.travellaboratory.be.infrastructure.domains.comment.entity.CommentLikeJpaEntity;
import site.travellaboratory.be.presentation.comment.dto.like.CommentToggleLikeResponse;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentJpaRepository commentJpaRepository;
    private final CommentLikeJpaRepository commentLikeJpaRepository;

    @Transactional
    public CommentToggleLikeResponse toggleLikeComment(Long userId, Long commentId) {
        // 유효하지 않은 댓글에 좋아요하려고 할 경우
        CommentJpaEntity commentJpaEntity = commentJpaRepository.findByIdAndStatusIn(commentId,
                List.of(CommentStatus.ACTIVE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.COMMENT_LIKE_INVALID,
                HttpStatus.NOT_FOUND));

        CommentLikeJpaEntity commentLikeJpaEntity = commentLikeJpaRepository.findByUserIdAndCommentId(userId,
                commentId)
            .orElse(null);

        // 댓글에 처음 좋아요를 누른 게 아닌 경우
        if (commentLikeJpaEntity != null) {
            commentLikeJpaEntity.toggleStatus();
        } else {
            // 댓글에 처음 좋아요를 누른 경우 - 새로 생성
            User user = User.of(userId);
            commentLikeJpaEntity = CommentLikeJpaEntity.of(user, commentJpaEntity);
        }

        CommentLikeJpaEntity saveLikeComment = commentLikeJpaRepository.save(commentLikeJpaEntity);
        return CommentToggleLikeResponse.from(saveLikeComment.getStatus());
    }
}
