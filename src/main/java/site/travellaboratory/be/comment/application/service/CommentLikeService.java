package site.travellaboratory.be.comment.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.comment.domain.Comment;
import site.travellaboratory.be.comment.domain.CommentLike;
import site.travellaboratory.be.comment.domain.enums.CommentLikeStatus;
import site.travellaboratory.be.comment.infrastructure.persistence.repository.CommentJpaRepository;
import site.travellaboratory.be.comment.domain.enums.CommentStatus;
import site.travellaboratory.be.user.infrastructure.persistence.repository.UserJpaRepository;
import site.travellaboratory.be.user.infrastructure.persistence.entity.UserEntity;
import site.travellaboratory.be.comment.infrastructure.persistence.repository.CommentLikeJpaRepository;
import site.travellaboratory.be.comment.infrastructure.persistence.entity.CommentLikeEntity;
import site.travellaboratory.be.user.domain.enums.UserStatus;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentJpaRepository commentJpaRepository;
    private final CommentLikeJpaRepository commentLikeJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Transactional
    public CommentLikeStatus toggleLike(Long userId, Long commentId) {
        // 유효하지 않은 댓글에 좋아요하려고 할 경우
        Comment comment = commentJpaRepository.findByIdAndStatusIn(commentId,
                List.of(CommentStatus.ACTIVE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.COMMENT_LIKE_INVALID,
                HttpStatus.NOT_FOUND)).toModel();

        CommentLikeEntity commentLikeEntity = commentLikeJpaRepository.findByUserIdAndCommentId(userId,
                commentId)
            .orElse(null);

        // 좋아요 누른 유저 가져오기
        UserEntity userEntity = userJpaRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        CommentLike commentLike;
        if (commentLikeEntity != null) {
            commentLike = commentLikeEntity.toModel().withToggleStatus();
        } else {
            commentLike = CommentLike.create(userEntity.toModel(), comment);
        }
        CommentLikeEntity saveCommentLike = commentLikeJpaRepository.save(CommentLikeEntity.from(commentLike));
        return saveCommentLike.getStatus();
    }
}
