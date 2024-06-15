package site.travellaboratory.be.domain.comment;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndStatusIn(Long commentId, List<CommentStatus> status);

}
