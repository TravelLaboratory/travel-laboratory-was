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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.travellaboratory.be.comment.domain.Comment;
import site.travellaboratory.be.comment.domain.enums.CommentStatus;
import site.travellaboratory.be.common.infrastructure.common.BaseEntity;
import site.travellaboratory.be.review.infrastructure.persistence.ReviewEntity;
import site.travellaboratory.be.user.infrastructure.persistence.entity.UserEntity;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private ReviewEntity reviewEntity;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String replyComment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommentStatus status;

    public static CommentEntity from(Comment comment) {
        CommentEntity result = new CommentEntity();
        result.id = comment.getId();
        result.userEntity = UserEntity.from(comment.getUser());
        result.reviewEntity = ReviewEntity.from(comment.getReview());
        result.replyComment = comment.getReplyComment();
        result.status = comment.getStatus();
        return result;
    }

    public Comment toModel() {
        return Comment.builder()
            .id(this.id)
            .user(this.userEntity.toModel())
            .review(this.reviewEntity.toModel())
            .replyComment(this.replyComment)
            .status(this.status)
            .build();
    }
}
