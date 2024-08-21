package site.travellaboratory.be.article.presentation.response.reader;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import site.travellaboratory.be.article.infrastructure.persistence.entity.ArticleLocationEntity;
import site.travellaboratory.be.article.infrastructure.persistence.entity.ArticleEntity;
import site.travellaboratory.be.article.domain.enums.TravelStyle;

public record ArticleOneResponse(
        String title,
        List<ArticleLocationEntity> locations,
        LocalDate startAt,
        LocalDate endAt,
        String expense,
        String coverImgUrl,
        String travelCompanion,
        List<String> travelStyles,
        String name,
        Long bookmarkCount,
        Boolean isBookmarked,
        Boolean isEditable
) {

    public static ArticleOneResponse of(
            final ArticleEntity articleEntity,
            final Long bookmarkCount,
            final Boolean isBookmarked,
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
                articleEntity.getCoverImgUrl(),
                articleEntity.getTravelCompanion().getName(),
                travelStyleNames,
                articleEntity.getUserEntity().getNickname(),
                bookmarkCount,
                isBookmarked,
                isEditable
        );
    }
}
