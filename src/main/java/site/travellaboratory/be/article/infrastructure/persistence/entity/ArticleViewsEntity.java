package site.travellaboratory.be.article.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.travellaboratory.be.article.domain.enums.ArticleViewsStatus;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArticleViewsStatus status;
}
