package site.travellaboratory.be.comment.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.comment.application.port.CommentLikeRepository;
import site.travellaboratory.be.comment.application.port.CommentRepository;
import site.travellaboratory.be.comment.domain.Comment;
import site.travellaboratory.be.comment.domain.CommentLike;
import site.travellaboratory.be.comment.domain.enums.CommentStatus;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.error.ErrorCodes;
import site.travellaboratory.be.user.application.port.UserRepository;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain.enums.UserStatus;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentLike toggleLike(Long userId, Long commentId) {
        // 유효하지 않은 댓글에 좋아요하려고 할 경우
        Comment comment = getCommentById(commentId);

        CommentLike commentLike = commentLikeRepository.findByUserIdAndCommentId(userId,
                commentId)
            .orElse(null);

        // 좋아요 누른 유저 가져오기
        User user = getUserById(userId);

        if (commentLike != null) {
            commentLike = commentLike.withToggleStatus();
        } else {
            commentLike = CommentLike.create(user, comment);
        }
        return commentLikeRepository.save(commentLike);
    }

    private User getUserById(Long userId) {
        return userRepository.getByIdAndStatus(userId, UserStatus.ACTIVE);
    }

    private Comment getCommentById(Long commentId) {
        return commentRepository.findByIdAndStatusIn(commentId,
                List.of(CommentStatus.ACTIVE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.COMMENT_LIKE_INVALID_COMMENT_ID,
                HttpStatus.NOT_FOUND));
    }
}
