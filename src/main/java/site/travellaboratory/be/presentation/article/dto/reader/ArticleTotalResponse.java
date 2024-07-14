package site.travellaboratory.be.presentation.article.dto.reader;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import site.travellaboratory.be.infrastructure.domains.article.entity.ArticleJpaEntity;
import site.travellaboratory.be.infrastructure.domains.article.entity.Location;
import site.travellaboratory.be.infrastructure.domains.article.enums.TravelStyle;

public record ArticleTotalResponse(
        Long articleId,
        String title,
        List<Location> location,
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
            final ArticleJpaEntity articleJpaEntity,
            final Long bookmarkCount,
            final Boolean isBookmarked,
            final Boolean isEditable
    ) {
        List<String> travelStyleNames = articleJpaEntity.getTravelStyles().stream()
                .map(TravelStyle::getName)
                .collect(Collectors.toList());

        return new ArticleTotalResponse(
                articleJpaEntity.getId(),
                articleJpaEntity.getTitle(),
                articleJpaEntity.getLocation(),
                articleJpaEntity.getStartAt(),
                articleJpaEntity.getEndAt(),
                articleJpaEntity.getExpense(),
                articleJpaEntity.getUserJpaEntity().getProfileImgUrl(),
                articleJpaEntity.getCoverImageUrl(),
                articleJpaEntity.getTravelCompanion().getName(),
                travelStyleNames,
                articleJpaEntity.getUserJpaEntity().getNickname(),
                bookmarkCount,
                isBookmarked,
                isEditable
        );
    }
}
