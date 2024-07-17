package site.travellaboratory.be.presentation.article.dto.reader;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import site.travellaboratory.be.domain.article.enums.TravelStyle;
import site.travellaboratory.be.infrastructure.domains.article.entity.ArticleEntity;
import site.travellaboratory.be.infrastructure.domains.article.entity.ArticleLocationEntity;

public record ArticleTotalResponse(
    Long articleId,
    String title,
    List<ArticleLocationEntity> location,
    LocalDate startAt,
    LocalDate endAt,
    String expense,
    String profileImageUrl,
    String coverImageUrl,
    String travelCompanion,
    List<String> travelStyles,
    String name,
    Long bookmarkCount,
    Boolean isBookmarked,
    Boolean isEditable

) {
    public static ArticleTotalResponse of(
            final ArticleEntity articleEntity,
            final Long bookmarkCount,
            final Boolean isBookmarked,
            final Boolean isEditable
    ) {
        List<String> travelStyleNames = articleEntity.getTravelStyles().stream()
                .map(TravelStyle::getName)
                .collect(Collectors.toList());

        return new ArticleTotalResponse(
                articleEntity.getId(),
                articleEntity.getTitle(),
                articleEntity.getLocationEntities(),
                articleEntity.getStartAt(),
                articleEntity.getEndAt(),
                articleEntity.getExpense(),
                articleEntity.getUserEntity().getProfileImgUrl(),
                articleEntity.getCoverImageUrl(),
                articleEntity.getTravelCompanion().getName(),
                travelStyleNames,
                articleEntity.getUserEntity().getNickname(),
                bookmarkCount,
                isBookmarked,
                isEditable
        );
    }
}
