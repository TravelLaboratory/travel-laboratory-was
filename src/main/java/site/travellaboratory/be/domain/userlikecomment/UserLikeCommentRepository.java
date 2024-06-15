package site.travellaboratory.be.domain.userlikecomment;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserLikeCommentRepository extends JpaRepository<UserLikeComment, Long> {

    @Query("SELECT t FROM UserLikeComment t WHERE t.user.id = :userId AND t.comment.id = :commentId")
    Optional<UserLikeComment> findByUserIdAndCommentId(@Param("userId") Long userId, @Param("commentId") Long commentId);
}
