package site.travellaboratory.be.domain.userlikecomment;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserLikeCommentRepository extends JpaRepository<UserLikeComment, Long> {

    // 댓글 좋아요
    @Query("SELECT t FROM UserLikeComment t WHERE t.user.id = :userId AND t.comment.id = :commentId")
    Optional<UserLikeComment> findByUserIdAndCommentId(@Param("userId") Long userId, @Param("commentId") Long commentId);

    // 댓글 전체 조회 (페이지네이션)
    // 좋아요 수
    // GROUP BY를 통해 commentId 별 좋아요 수를 계산
    @Query("SELECT t.comment.id, COUNT(t) FROM UserLikeComment t WHERE t.comment.id IN :commentIds AND t.status = :status GROUP BY t.comment.id")
    List<Object[]> countByCommentIdsAndStatusGroupByCommentId(@Param("commentIds") List<Long> commentIds, @Param("status") UserLikeCommentStatus status);

    // 좋아요 여부
    // userId와 commentIds 리스트 (getComment 하기에 FETCH JOIN 사용

    // before
    @Query("SELECT t FROM UserLikeComment t WHERE t.user.id = :userId AND t.comment.id IN :commentIds AND t.status = :status")
    List<UserLikeComment> findAllByUserIdAndCommentIdInAndStatus(Long userId,
        Collection<Long> commentIds, UserLikeCommentStatus status);

    // after
    @Query("SELECT t FROM UserLikeComment t LEFT JOIN FETCH t.comment WHERE t.user.id = :userId AND t.comment.id IN :commentIds AND t.status = :status")
    List<UserLikeComment> findAllByUserIdAndCommentIdInAndStatusFetchJoinComment(@Param("userId") Long userId, @Param("commentIds") Collection<Long> commentIds, @Param("status") UserLikeCommentStatus status);
}
