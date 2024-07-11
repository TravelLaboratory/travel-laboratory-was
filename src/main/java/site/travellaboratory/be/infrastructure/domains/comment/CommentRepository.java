package site.travellaboratory.be.infrastructure.domains.comment;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.travellaboratory.be.infrastructure.domains.comment.entity.Comment;
import site.travellaboratory.be.infrastructure.domains.comment.enums.CommentStatus;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndStatusIn(Long commentId, List<CommentStatus> status);

    // 댓글 리스트 조회 + pagination
    Page<Comment> findByReviewIdAndStatusOrderByCreatedAtDesc(Long reviewId, CommentStatus status, Pageable pageable);

    // 댓글 리스트 조회에서 수정,삭제 권한 체크 시 N+1 문제 해결을 위해 먼저 관련된 User 가져오는 메서드
    @Query("SELECT t FROM Comment t LEFT JOIN FETCH t.user WHERE t.id IN :commentIds AND t.status = :status ORDER BY t.createdAt DESC")
    List<Comment> findAllByIdInFetchJoinUser(@Param("commentIds") List<Long> commentIds, @Param("status") CommentStatus status);
}
