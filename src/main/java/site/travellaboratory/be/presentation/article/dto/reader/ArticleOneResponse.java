package site.travellaboratory.be.presentation.article.dto.reader;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import site.travellaboratory.be.infrastructure.domains.article.entity.ArticleLocationEntity;
import site.travellaboratory.be.infrastructure.domains.article.entity.ArticleEntity;
import site.travellaboratory.be.domain.article.enums.TravelStyle;

public record ArticleOneResponse(
        String title,
        List<ArticleLocationEntity> location,
        LocalDate startAt,
        LocalDate endAt,
        String expense,
        String coverImage,
        String travelCompanion,
        List<String> travelStyles,
        String name,
        Long bookmarkCount,
        Boolean isBookmarked,
        Boolean isPrivate,
        Boolean isEditable
) {

    public static ArticleOneResponse of(
            final ArticleEntity articleEntity,
            final Long bookmarkCount,
            final Boolean isBookmarked,
            final Boolean isPrivate,
            final Boolean isEditable
    ) {
        List<String> travelStyleNames = articleEntity.getTravelStyles().stream()
                .map(TravelStyle::getName)
                .collect(Collectors.toList());

        return new ArticleOneResponse(
                articleEntity.getTitle(),
                articleEntity.getLocationEntities(),
                articleEntity.getStartAt(),
                articleEntity.getEndAt(),
                articleEntity.getExpense(),
                articleEntity.getCoverImageUrl(),
                articleEntity.getTravelCompanion().getName(),
                travelStyleNames,
                articleEntity.getUserEntity().getNickname(),
                bookmarkCount,
                isBookmarked,
                isPrivate,
                isEditable
        );
    }
}
