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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.travellaboratory.be.domain.comment.Comment;
import site.travellaboratory.be.domain.comment.enums.CommentStatus;
import site.travellaboratory.be.infrastructure.common.BaseEntity;
import site.travellaboratory.be.infrastructure.domains.review.entity.ReviewJpaEntity;
import site.travellaboratory.be.infrastructure.domains.user.entity.UserJpaEntity;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserJpaEntity userJpaEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private ReviewJpaEntity reviewJpaEntity;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String replyContent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommentStatus status;

    public static CommentJpaEntity from(Comment comment) {
        CommentJpaEntity result = new CommentJpaEntity();
        result.id = comment.getId();
        result.userJpaEntity = UserJpaEntity.from(comment.getUser());
        result.reviewJpaEntity = ReviewJpaEntity.from(comment.getReview());
        result.replyContent = comment.getReplyContent();
        result.status = comment.getStatus();
        return result;
    }

    public Comment toModel() {
        return Comment.builder()
            .id(this.id)
            .user(this.userJpaEntity.toModel())
            .review(this.reviewJpaEntity.toModel())
            .replyContent(this.replyContent)
            .status(this.status)
            .build();
    }
}
