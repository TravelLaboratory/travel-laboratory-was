package site.travellaboratory.be.comment.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.travellaboratory.be.comment.infrastructure.persistence.entity.CommentEntity;
import site.travellaboratory.be.comment.domain.enums.CommentStatus;

public interface CommentJpaRepository extends JpaRepository<CommentEntity, Long> {
    Optional<CommentEntity> findByIdAndStatus(Long commentId, CommentStatus status);

    // 댓글 리스트 조회 + pagination
    Page<CommentEntity> findByReviewEntityIdAndStatusOrderByCreatedAtDesc(Long reviewId, CommentStatus status, Pageable pageable);

    // 댓글 리스트 조회에서 수정,삭제 권한 체크 시 N+1 문제 해결을 위해 먼저 관련된 User 가져오는 메서드
    @Query("SELECT t FROM CommentEntity t LEFT JOIN FETCH t.userEntity WHERE t.id IN :commentIds AND t.status = :status ORDER BY t.createdAt DESC")
    List<CommentEntity> findAllByIdInFetchJoinUser(@Param("commentIds") List<Long> commentIds, @Param("status") CommentStatus status);
}
