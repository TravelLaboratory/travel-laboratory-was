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
import site.travellaboratory.be.infrastructure.common.BaseEntity;
import site.travellaboratory.be.infrastructure.domains.user.entity.User;
import site.travellaboratory.be.infrastructure.domains.userlikecomment.enums.UserLikeCommentStatus;

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
    private UserLikeCommentStatus status;

    private CommentLikeJpaEntity(User user, CommentJpaEntity commentJpaEntity) {
        this.user = user;
        this.commentJpaEntity = commentJpaEntity;
        this.status = UserLikeCommentStatus.ACTIVE;
    }

    public static CommentLikeJpaEntity of(User user, CommentJpaEntity commentJpaEntity) {
        return new CommentLikeJpaEntity(user, commentJpaEntity);
    }

    public void toggleStatus() {
        if (this.status == UserLikeCommentStatus.ACTIVE) {
            this.status = UserLikeCommentStatus.INACTIVE;
        } else {
            this.status = UserLikeCommentStatus.ACTIVE;
        }
    }
}