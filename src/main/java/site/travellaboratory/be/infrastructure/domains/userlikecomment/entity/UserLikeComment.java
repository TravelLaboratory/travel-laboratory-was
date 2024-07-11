package site.travellaboratory.be.infrastructure.domains.userlikecomment.entity;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.travellaboratory.be.infrastructure.common.BaseEntity;
import site.travellaboratory.be.infrastructure.domains.comment.entity.Comment;
import site.travellaboratory.be.infrastructure.domains.user.entity.User;
import site.travellaboratory.be.infrastructure.domains.userlikecomment.enums.UserLikeCommentStatus;

@Entity
@Getter
@NoArgsConstructor
public class UserLikeComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserLikeCommentStatus status;

    private UserLikeComment(User user, Comment comment) {
        this.user = user;
        this.comment = comment;
        this.status = UserLikeCommentStatus.ACTIVE;
    }

    public static UserLikeComment of(User user, Comment comment) {
        return new UserLikeComment(user, comment);
    }

    public void toggleStatus() {
        if (this.status == UserLikeCommentStatus.ACTIVE) {
            this.status = UserLikeCommentStatus.INACTIVE;
        } else {
            this.status = UserLikeCommentStatus.ACTIVE;
        }
    }
}