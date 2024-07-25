package site.travellaboratory.be.comment.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.travellaboratory.be.comment.application.port.CommentRepository;
import site.travellaboratory.be.comment.domain.Comment;
import site.travellaboratory.be.comment.domain.enums.CommentStatus;
import site.travellaboratory.be.comment.infrastructure.persistence.entity.CommentEntity;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

    private final CommentJpaRepository commentJpaRepository;

    @Override
    public Optional<Comment> findByIdAndStatusIn(Long commentId, List<CommentStatus> status) {
        return commentJpaRepository.findByIdAndStatusIn(commentId, status).map(CommentEntity::toModel);
    }

    @Override
    public Comment save(Comment comment) {
        return commentJpaRepository.save(CommentEntity.from(comment)).toModel();
    }
}
