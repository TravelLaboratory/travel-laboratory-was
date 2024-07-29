package site.travellaboratory.be.comment.infrastructure.persistence.repository;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.travellaboratory.be.comment.application.port.CommentLikeRepository;
import site.travellaboratory.be.comment.domain.CommentLike;
import site.travellaboratory.be.comment.infrastructure.persistence.entity.CommentLikeEntity;

@Repository
@RequiredArgsConstructor
public class CommentLikeRepositoryImpl implements CommentLikeRepository {

    private final CommentLikeJpaRepository commentLikeJpaRepository;

    @Override
    public Optional<CommentLike> findByUserIdAndCommentId(Long userId, Long commentId) {
        return commentLikeJpaRepository.findByUserIdAndCommentId(userId, commentId).map(
            CommentLikeEntity::toModel);
    }

    @Override
    public CommentLike save(CommentLike commentLike) {
        return commentLikeJpaRepository.save(CommentLikeEntity.from(commentLike)).toModel();
    }
}
