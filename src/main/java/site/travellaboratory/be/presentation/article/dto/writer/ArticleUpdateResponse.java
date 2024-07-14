package site.travellaboratory.be.presentation.article.dto.writer;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import site.travellaboratory.be.infrastructure.domains.article.entity.ArticleJpaEntity;
import site.travellaboratory.be.infrastructure.domains.article.entity.Location;
import site.travellaboratory.be.infrastructure.domains.article.enums.TravelStyle;

public record ArticleUpdateResponse(
        String title,
        List<Location> location,
        LocalDate startAt,
        LocalDate endAt,
        String expense,
        String travelCompanion,
        List<String> travelStyles

) {
    public static ArticleUpdateResponse from(final ArticleJpaEntity articleJpaEntity) {
        List<String> travelStyleNames = articleJpaEntity.getTravelStyles()
                .stream()
                .map(TravelStyle::getName)
                .collect(Collectors.toList());

        return new ArticleUpdateResponse(
                articleJpaEntity.getTitle(),
                articleJpaEntity.getLocation(),
                articleJpaEntity.getStartAt(),
                articleJpaEntity.getEndAt(),
                articleJpaEntity.getExpense(),
                articleJpaEntity.getTravelCompanion().getName(),
                travelStyleNames
        );
    }
}
