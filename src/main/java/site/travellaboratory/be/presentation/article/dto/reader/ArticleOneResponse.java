package site.travellaboratory.be.presentation.article.dto.reader;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import site.travellaboratory.be.infrastructure.domains.article.entity.ArticleLocationJpaEntity;
import site.travellaboratory.be.infrastructure.domains.article.entity.ArticleJpaEntity;
import site.travellaboratory.be.domain.article.enums.TravelStyle;

public record ArticleOneResponse(
        String title,
        List<ArticleLocationJpaEntity> locationJpaEntities,
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
            final ArticleJpaEntity articleJpaEntity,
            final Long bookmarkCount,
            final Boolean isBookmarked,
            final Boolean isPrivate,
            final Boolean isEditable
    ) {
        List<String> travelStyleNames = articleJpaEntity.getTravelStyles().stream()
                .map(TravelStyle::getName)
                .collect(Collectors.toList());

        return new ArticleOneResponse(
                articleJpaEntity.getTitle(),
                articleJpaEntity.getLocationJpaEntities(),
                articleJpaEntity.getStartAt(),
                articleJpaEntity.getEndAt(),
                articleJpaEntity.getExpense(),
                articleJpaEntity.getCoverImageUrl(),
                articleJpaEntity.getTravelCompanion().getName(),
                travelStyleNames,
                articleJpaEntity.getUserJpaEntity().getNickname(),
                bookmarkCount,
                isBookmarked,
                isPrivate,
                isEditable
        );
    }
}
