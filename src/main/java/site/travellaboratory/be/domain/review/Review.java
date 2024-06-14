package site.travellaboratory.be.domain.review;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.travellaboratory.be.domain.BaseEntity;
import site.travellaboratory.be.domain.article.Article;
import site.travellaboratory.be.domain.user.entity.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    uniqueConstraints = @UniqueConstraint(columnNames = {"article_id"}) // 하나의 여행 계획에는 하나의 리뷰만 가능 (기획)
)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 255)
    private String representativeImgUrl;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewStatus status;

    public Review(
        User user,
        Article article,
        String title,
        String representativeImgUrl,
        String description)
    {
        this.user = user;
        this.article = article;
        this.title = title;
        this.representativeImgUrl = representativeImgUrl;
        this.description = description;
        this.status = ReviewStatus.ACTIVE;
    }

    public static Review of(
        final User user,
        final Article article,
        final String title,
        final String representativeImgUrl,
        final String description
    ) {
        return new Review(user, article, title, representativeImgUrl, description);
    }

    public void update(String title, String representativeImgUrl, String description) {
        this.title = title;
        this.representativeImgUrl = representativeImgUrl;
        this.description = description;
    }

    public void delete() {
        this.status = ReviewStatus.INACTIVE;
    }
}
