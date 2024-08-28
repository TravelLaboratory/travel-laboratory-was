package site.travellaboratory.be.article.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.travellaboratory.be.article.domain._views.ArticleViews;
import site.travellaboratory.be.common.infrastructure.common.BaseEntity;

@Entity
@Table(name = "article_views")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleViewsEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long articleId;

    public static ArticleViewsEntity from(ArticleViews articleViews) {
        ArticleViewsEntity result = new ArticleViewsEntity();
        result.id = articleViews.getId();
        result.userId = articleViews.getUserId();
        result.articleId = articleViews.getArticleId();
        result.setCreatedAt(articleViews.getCreatedAt());
        result.setUpdatedAt(articleViews.getUpdatedAt());
        return result;
    }

    public ArticleViews toModel() {
        return ArticleViews.builder()
            .id(this.id)
            .userId(this.userId)
            .articleId(this.articleId)
            .createdAt(this.getCreatedAt())
            .updatedAt(this.getUpdatedAt())
            .build();
    }
}
