package site.travellaboratory.be.infrastructure.domains.comment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.travellaboratory.be.domain.comment.CommentLike;
import site.travellaboratory.be.domain.comment.enums.CommentLikeStatus;
import site.travellaboratory.be.infrastructure.common.BaseEntity;
import site.travellaboratory.be.infrastructure.domains.user.entity.User;

@Entity
@Table(name = "comment_like")
@Getter
@NoArgsConstructor
public class CommentLikeJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private CommentJpaEntity commentJpaEntity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommentLikeStatus status;

    public static CommentLikeJpaEntity from(CommentLike commentLike) {
        CommentLikeJpaEntity result = new CommentLikeJpaEntity();
        result.id = commentLike.getId();
        result.user = commentLike.getUser();
        result.commentJpaEntity = CommentJpaEntity.from(commentLike.getComment());
        result.status = commentLike.getStatus();
        return result;
    }

    public CommentLike toModel() {
        return CommentLike.builder()
            .id(this.getId())
            .user(this.getUser())
            .comment(this.getCommentJpaEntity().toModel())
            .status(this.getStatus())
            .build();
    }
}