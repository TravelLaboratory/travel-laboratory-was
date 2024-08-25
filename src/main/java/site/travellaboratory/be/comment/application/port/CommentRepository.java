package site.travellaboratory.be.comment.application.port;

import java.util.Optional;
import site.travellaboratory.be.comment.domain.Comment;
import site.travellaboratory.be.comment.domain.enums.CommentStatus;

public interface CommentRepository {
    Optional<Comment> findByIdAndStatus(Long commentId, CommentStatus status);

    Comment save(Comment comment);
}
