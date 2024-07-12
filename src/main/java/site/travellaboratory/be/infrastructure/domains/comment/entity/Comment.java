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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.travellaboratory.be.infrastructure.common.BaseEntity;
import site.travellaboratory.be.infrastructure.domains.comment.enums.CommentStatus;
import site.travellaboratory.be.infrastructure.domains.user.entity.User;
import site.travellaboratory.be.infrastructure.domains.review.entity.ReviewJpaEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private ReviewJpaEntity reviewJpaEntity;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String replyContent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommentStatus status;

    private Comment(
        User user,
        ReviewJpaEntity reviewJpaEntity,
        String replyContent
    ) {
        this.user = user;
        this.reviewJpaEntity = reviewJpaEntity;
        this.replyContent = replyContent;
        this.status = CommentStatus.ACTIVE;
    }

    public static Comment of(
        User user,
        ReviewJpaEntity reviewJpaEntity,
        String replyContent
    ) {
        return new Comment(user, reviewJpaEntity, replyContent);
    }

    public void update(String replyContent) {
        this.replyContent = replyContent;
    }

    public void delete() {
        this.status = CommentStatus.INACTIVE;
    }
}
