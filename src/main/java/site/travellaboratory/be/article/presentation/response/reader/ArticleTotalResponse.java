package site.travellaboratory.be.article.presentation.response.reader;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import site.travellaboratory.be.article.domain.enums.TravelStyle;
import site.travellaboratory.be.article.infrastructure.persistence.entity.ArticleEntity;
import site.travellaboratory.be.article.infrastructure.persistence.entity.ArticleLocationEntity;

public record ArticleTotalResponse(
        Long userId, // 글쓴이 ID
        String nickname, // 글쓴이 닉네임닉네임
        String profileImgUrl,
        Long articleId,
        String title,
        List<ArticleLocationEntity> locations,
        LocalDate startAt,
        LocalDate endAt,
        String expense,
        String coverImgUrl,
        String travelCompanion,
        List<String> travelStyles,
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
                articleEntity.getUserEntity().getId(),
                articleEntity.getUserEntity().getNickname(),
                articleEntity.getUserEntity().getProfileImgUrl(),
                articleEntity.getId(),
                articleEntity.getTitle(),
                articleEntity.getLocationEntities(),
                articleEntity.getStartAt(),
                articleEntity.getEndAt(),
                articleEntity.getExpense(),
                articleEntity.getCoverImgUrl(),
                articleEntity.getTravelCompanion().getName(),
                travelStyleNames,
                bookmarkCount,
                isBookmarked,
                isEditable
        );
    }
}
