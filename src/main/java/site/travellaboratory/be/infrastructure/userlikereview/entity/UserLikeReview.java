package site.travellaboratory.be.infrastructure.userlikereview.entity;

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
import site.travellaboratory.be.infrastructure.BaseEntity;
import site.travellaboratory.be.infrastructure.review.entity.Review;
import site.travellaboratory.be.infrastructure.user.entity.User;
import site.travellaboratory.be.infrastructure.userlikereview.enums.UserLikeReviewStatus;

@Entity
@Getter
@NoArgsConstructor
public class UserLikeReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserLikeReviewStatus status;

    public static UserLikeReview of(User user, Review review) {
        return new UserLikeReview(user, review);
    }

    private UserLikeReview(User user, Review review) {
        this.user = user;
        this.review = review;
        this.status = UserLikeReviewStatus.ACTIVE;
    }

    public void toggleStatus() {
        if (this.status == UserLikeReviewStatus.ACTIVE) {
            this.status = UserLikeReviewStatus.INACTIVE;
        } else {
            this.status = UserLikeReviewStatus.ACTIVE;
        }
    }
}
