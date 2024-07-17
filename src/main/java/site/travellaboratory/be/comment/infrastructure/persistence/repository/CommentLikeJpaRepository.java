package site.travellaboratory.be.comment.infrastructure.persistence.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.travellaboratory.be.comment.domain.enums.CommentLikeStatus;
import site.travellaboratory.be.comment.infrastructure.persistence.entity.CommentLikeEntity;

public interface CommentLikeJpaRepository extends JpaRepository<CommentLikeEntity, Long> {

    // 댓글 좋아요
    @Query("SELECT t FROM CommentLikeEntity t WHERE t.userEntity.id = :userId AND t.commentEntity.id = :commentId")
    Optional<CommentLikeEntity> findByUserIdAndCommentId(@Param("userId") Long userId, @Param("commentId") Long commentId);

    // 댓글 전체 조회 (페이지네이션)
    // 좋아요 수
    // GROUP BY를 통해 commentId 별 좋아요 수를 계산
    @Query("SELECT t.commentEntity.id, COUNT(t) FROM CommentLikeEntity t WHERE t.commentEntity.id IN :commentIds AND t.status = :status GROUP BY t.commentEntity.id")
    List<Object[]> countByCommentIdsAndStatusGroupByCommentId(@Param("commentIds") List<Long> commentIds, @Param("status") CommentLikeStatus status);

    // 좋아요 여부
    // userId와 commentIds 리스트 (getComment 하기에 FETCH JOIN 사용

    // before
    @Query("SELECT t FROM CommentLikeEntity t WHERE t.userEntity.id = :userId AND t.commentEntity.id IN :commentIds AND t.status = :status")
    List<CommentLikeEntity> findAllByUserIdAndCommentIdInAndStatus(Long userId,
        Collection<Long> commentIds, CommentLikeStatus status);

    // after
    @Query("SELECT t FROM CommentLikeEntity t LEFT JOIN FETCH t.commentEntity WHERE t.userEntity.id = :userId AND t.commentEntity.id IN :commentIds AND t.status = :status")
    List<CommentLikeEntity> findAllByUserIdAndCommentIdInAndStatusFetchJoinComment(@Param("userId") Long userId, @Param("commentIds") Collection<Long> commentIds, @Param("status") CommentLikeStatus status);
}
