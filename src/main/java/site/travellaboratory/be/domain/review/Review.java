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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.travellaboratory.be.domain.BaseEntity;
import site.travellaboratory.be.domain.article.Article;
import site.travellaboratory.be.domain.user.entity.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
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

    private Review(
        User user,
        Article article,
        String title,
        String representativeImgUrl,
        String description,
        ReviewStatus status)
    {
        this.user = user;
        this.article = article;
        this.title = title;
        this.representativeImgUrl = representativeImgUrl;
        this.description = description;
        this.status = status;
    }

    public static Review of(
        final User user,
        final Article article,
        final String title,
        final String representativeImgUrl,
        final String description,
        final ReviewStatus status
    ) {
        return new Review(user, article, title, representativeImgUrl, description, status);
    }

    public void update(String title, String representativeImgUrl, String description, ReviewStatus status) {
        this.title = title;
        this.representativeImgUrl = representativeImgUrl;
        this.description = description;
        this.status = status;
    }

    public void delete() {
        this.status = ReviewStatus.INACTIVE;
    }
}
