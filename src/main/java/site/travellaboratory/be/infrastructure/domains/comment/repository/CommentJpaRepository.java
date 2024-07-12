package site.travellaboratory.be.infrastructure.domains.comment.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.travellaboratory.be.infrastructure.domains.comment.entity.CommentJpaEntity;
import site.travellaboratory.be.domain.comment.enums.CommentStatus;

public interface CommentJpaRepository extends JpaRepository<CommentJpaEntity, Long> {
    Optional<CommentJpaEntity> findByIdAndStatusIn(Long commentId, List<CommentStatus> status);

    // 댓글 리스트 조회 + pagination
    Page<CommentJpaEntity> findByReviewJpaEntityIdAndStatusOrderByCreatedAtDesc(Long reviewId, CommentStatus status, Pageable pageable);

    // 댓글 리스트 조회에서 수정,삭제 권한 체크 시 N+1 문제 해결을 위해 먼저 관련된 User 가져오는 메서드
    @Query("SELECT t FROM CommentJpaEntity t LEFT JOIN FETCH t.user WHERE t.id IN :commentIds AND t.status = :status ORDER BY t.createdAt DESC")
    List<CommentJpaEntity> findAllByIdInFetchJoinUser(@Param("commentIds") List<Long> commentIds, @Param("status") CommentStatus status);
}
