package site.travellaboratory.be.infrastructure.domains.comment.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.travellaboratory.be.domain.comment.enums.CommentLikeStatus;
import site.travellaboratory.be.infrastructure.domains.comment.entity.CommentLikeJpaEntity;

public interface CommentLikeJpaRepository extends JpaRepository<CommentLikeJpaEntity, Long> {

    // 댓글 좋아요
    @Query("SELECT t FROM CommentLikeJpaEntity t WHERE t.user.id = :userId AND t.commentJpaEntity.id = :commentId")
    Optional<CommentLikeJpaEntity> findByUserIdAndCommentId(@Param("userId") Long userId, @Param("commentId") Long commentId);

    // 댓글 전체 조회 (페이지네이션)
    // 좋아요 수
    // GROUP BY를 통해 commentId 별 좋아요 수를 계산
    @Query("SELECT t.commentJpaEntity.id, COUNT(t) FROM CommentLikeJpaEntity t WHERE t.commentJpaEntity.id IN :commentIds AND t.status = :status GROUP BY t.commentJpaEntity.id")
    List<Object[]> countByCommentIdsAndStatusGroupByCommentId(@Param("commentIds") List<Long> commentIds, @Param("status") CommentLikeStatus status);

    // 좋아요 여부
    // userId와 commentIds 리스트 (getComment 하기에 FETCH JOIN 사용

    // before
    @Query("SELECT t FROM CommentLikeJpaEntity t WHERE t.user.id = :userId AND t.commentJpaEntity.id IN :commentIds AND t.status = :status")
    List<CommentLikeJpaEntity> findAllByUserIdAndCommentIdInAndStatus(Long userId,
        Collection<Long> commentIds, CommentLikeStatus status);

    // after
    @Query("SELECT t FROM CommentLikeJpaEntity t LEFT JOIN FETCH t.commentJpaEntity WHERE t.user.id = :userId AND t.commentJpaEntity.id IN :commentIds AND t.status = :status")
    List<CommentLikeJpaEntity> findAllByUserIdAndCommentIdInAndStatusFetchJoinComment(@Param("userId") Long userId, @Param("commentIds") Collection<Long> commentIds, @Param("status") CommentLikeStatus status);
}
