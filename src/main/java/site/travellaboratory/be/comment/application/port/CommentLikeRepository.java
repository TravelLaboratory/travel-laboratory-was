package site.travellaboratory.be.comment.application.port;

import java.util.Optional;
import site.travellaboratory.be.comment.domain.CommentLike;

public interface CommentLikeRepository {

    Optional<CommentLike> findByUserIdAndCommentId(Long userId, Long commentId);

    CommentLike save(CommentLike commentLike);
}
