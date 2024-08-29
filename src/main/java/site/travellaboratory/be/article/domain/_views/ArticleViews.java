package site.travellaboratory.be.article.domain._views;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ArticleViews {

    private final Long id;
    private final Long userId;
    private final Long articleId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static ArticleViews create(Long userId, Long articleId) {
        return ArticleViews.builder()
            .userId(userId)
            .articleId(articleId)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    public ArticleViews withUpdatedAt() {
        return ArticleViews.builder()
            .id(this.id)
            .userId(userId)
            .articleId(articleId)
            .createdAt(this.getCreatedAt())
            .updatedAt(LocalDateTime.now())
            .build();
    }
}
