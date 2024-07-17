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
import site.travellaboratory.be.infrastructure.domains.article.entity.ArticleEntity;
import site.travellaboratory.be.infrastructure.domains.bookmark.enums.BookmarkStatus;
import site.travellaboratory.be.infrastructure.domains.user.entity.UserEntity;

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
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private ArticleEntity articleEntity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookmarkStatus status;

    public Bookmark(final UserEntity userEntity, final ArticleEntity articleEntity) {
        this.userEntity = userEntity;
        this.articleEntity = articleEntity;
        this.status = BookmarkStatus.ACTIVE;
    }

    public static Bookmark of(final UserEntity userEntity,
        final ArticleEntity articleEntity) {
        return new Bookmark(userEntity, articleEntity);
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
