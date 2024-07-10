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
import site.travellaboratory.be.infrastructure.user.entity.User;
import site.travellaboratory.be.infrastructure.userlikecomment.UserLikeCommentRepository;
import site.travellaboratory.be.infrastructure.userlikecomment.entity.UserLikeComment;
import site.travellaboratory.be.presentation.comment.dto.like.CommentToggleLikeResponse;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentRepository commentRepository;
    private final UserLikeCommentRepository userLikeCommentRepository;

    @Transactional
    public CommentToggleLikeResponse toggleLikeComment(Long userId, Long commentId) {
        // 유효하지 않은 댓글에 좋아요하려고 할 경우
        Comment comment = commentRepository.findByIdAndStatusIn(commentId,
                List.of(CommentStatus.ACTIVE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.COMMENT_LIKE_INVALID,
                HttpStatus.NOT_FOUND));

        UserLikeComment userLikeComment = userLikeCommentRepository.findByUserIdAndCommentId(userId,
                commentId)
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
