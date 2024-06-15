package site.travellaboratory.be.domain.comment;

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
import site.travellaboratory.be.domain.BaseEntity;
import site.travellaboratory.be.domain.review.Review;
import site.travellaboratory.be.domain.user.entity.User;

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
    private Review review;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String replyContent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommentStatus status;

    private Comment(
        User user,
        Review review,
        String replyContent
    ) {
        this.user = user;
        this.review = review;
        this.replyContent = replyContent;
        this.status = CommentStatus.ACTIVE;
    }

    public static Comment of(
        User user,
        Review review,
        String replyContent
    ) {
        return new Comment(user, review, replyContent);
    }

    public void update(String replyContent) {
        this.replyContent = replyContent;
    }

    public void delete() {
        this.status = CommentStatus.INACTIVE;
    }
}
