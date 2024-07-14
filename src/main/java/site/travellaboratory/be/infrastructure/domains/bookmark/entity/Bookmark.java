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
import site.travellaboratory.be.infrastructure.domains.article.entity.ArticleJpaEntity;
import site.travellaboratory.be.infrastructure.domains.bookmark.enums.BookmarkStatus;
import site.travellaboratory.be.infrastructure.domains.user.entity.UserJpaEntity;

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
    private UserJpaEntity userJpaEntity;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private ArticleJpaEntity articleJpaEntity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookmarkStatus status;

    public Bookmark(final UserJpaEntity userJpaEntity, final ArticleJpaEntity articleJpaEntity) {
        this.userJpaEntity = userJpaEntity;
        this.articleJpaEntity = articleJpaEntity;
        this.status = BookmarkStatus.ACTIVE;
    }

    public static Bookmark of(final UserJpaEntity userJpaEntity,
        final ArticleJpaEntity articleJpaEntity) {
        return new Bookmark(userJpaEntity, articleJpaEntity);
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
