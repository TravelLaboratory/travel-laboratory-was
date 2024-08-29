package site.travellaboratory.be.article.presentation.response.reader;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import site.travellaboratory.be.article.domain.enums.TravelStyle;
import site.travellaboratory.be.article.infrastructure.persistence.entity.ArticleEntity;
import site.travellaboratory.be.article.infrastructure.persistence.entity.ArticleLocationEntity;

public record BannerArticlesResponse(
    Long articleId,
    String title,
    List<ArticleLocationEntity> locations,
    LocalDate startAt,
    LocalDate endAt,
    String expense,
    String coverImgUrl,
    String travelCompanion,
    List<String> travelStyles,
    String writer,
    String profileImgUrl
) {
    public static BannerArticlesResponse of(
            final ArticleEntity articleEntity) {
        List<String> travelStyleNames = articleEntity.getTravelStyles().stream()
                .map(TravelStyle::getName)
                .collect(Collectors.toList());

        return new BannerArticlesResponse(
                articleEntity.getId(),
                articleEntity.getTitle(),
                articleEntity.getLocationEntities(),
                articleEntity.getStartAt(),
                articleEntity.getEndAt(),
                articleEntity.getExpense(),
                articleEntity.getCoverImgUrl(),
                articleEntity.getTravelCompanion().getName(),
                travelStyleNames,
                articleEntity.getUserEntity().getNickname(),
                articleEntity.getUserEntity().getProfileImgUrl()
        );
    }
}
