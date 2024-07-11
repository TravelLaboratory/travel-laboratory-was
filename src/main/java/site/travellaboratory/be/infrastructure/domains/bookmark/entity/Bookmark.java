package site.travellaboratory.be.infrastructure.domains.bookmark.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.travellaboratory.be.infrastructure.common.BaseEntity;
import site.travellaboratory.be.infrastructure.domains.article.entity.Article;
import site.travellaboratory.be.infrastructure.domains.bookmark.enums.BookmarkStatus;
import site.travellaboratory.be.infrastructure.domains.user.entity.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "bookmark")
public class Bookmark extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookmarkStatus status;

    public Bookmark(final User user, final Article article) {
        this.user = user;
        this.article = article;
        this.status = BookmarkStatus.ACTIVE;
    }

    public static Bookmark of(final User user, final Article article) {
        return new Bookmark(user, article);
    }

    public void toggleStatus() {
        if (this.status == BookmarkStatus.ACTIVE) {
            this.status = BookmarkStatus.INACTIVE;
        } else {
            this.status = BookmarkStatus.ACTIVE;
        }
    }

    public void updateStatus(final BookmarkStatus status) {
        this.status = status;
    }
}
