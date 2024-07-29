package site.travellaboratory.be.comment.infrastructure.persistence.entity;

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
import site.travellaboratory.be.comment.domain.CommentLike;
import site.travellaboratory.be.comment.domain.enums.CommentLikeStatus;
import site.travellaboratory.be.common.infrastructure.common.BaseEntity;
import site.travellaboratory.be.user.infrastructure.persistence.entity.UserEntity;

@Entity
@Table(name = "comment_like")
@Getter
@NoArgsConstructor
public class CommentLikeEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private CommentEntity commentEntity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommentLikeStatus status;

    public static CommentLikeEntity from(CommentLike commentLike) {
        CommentLikeEntity result = new CommentLikeEntity();
        result.id = commentLike.getId();
        result.userEntity = UserEntity.from(commentLike.getUser());
        result.commentEntity = CommentEntity.from(commentLike.getComment());
        result.status = commentLike.getStatus();
        result.setCreatedAt(commentLike.getCreatedAt());
        result.setUpdatedAt(commentLike.getUpdatedAt());
        return result;
    }

    public CommentLike toModel() {
        return CommentLike.builder()
            .id(this.getId())
            .user(this.getUserEntity().toModel())
            .comment(this.getCommentEntity().toModel())
            .status(this.getStatus())
            .createdAt(this.getCreatedAt())
            .updatedAt(this.getUpdatedAt())
            .build();
    }
}