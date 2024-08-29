package site.travellaboratory.be.article.presentation.response.like;

import java.time.LocalDate;
import java.util.List;
import site.travellaboratory.be.article.infrastructure.persistence.entity.ArticleLocationEntity;
import site.travellaboratory.be.article.domain.enums.TravelStyle;
import site.travellaboratory.be.article.infrastructure.persistence.entity.BookmarkEntity;

public record BookmarkResponse(
        Long articleId,
        String title,
        List<ArticleLocationEntity> locations,
        LocalDate startAt,
        LocalDate endAt,
        String expense,
        String profileImgUrl,
        String coverImgUrl,
        String travelCompanion,
        List<String> travelStyles,
        String name,
        Long bookmarkCount,
        Boolean isBookmarked
) {

    public static BookmarkResponse of(final BookmarkEntity bookmarkEntity, final Long bookmarkCount, final Boolean isBookmarked) {
        List<String> travelStyleNames = bookmarkEntity.getArticleEntity().getTravelStyles().stream()
                .map(TravelStyle::getName)
                .toList();

        return new BookmarkResponse(
                bookmarkEntity.getArticleEntity().getId(),
                bookmarkEntity.getArticleEntity().getTitle(),
                bookmarkEntity.getArticleEntity().getLocationEntities(),
                bookmarkEntity.getArticleEntity().getStartAt(),
                bookmarkEntity.getArticleEntity().getEndAt(),
                bookmarkEntity.getArticleEntity().getExpense(),
                bookmarkEntity.getArticleEntity().getUserEntity().getProfileImgUrl(),
                bookmarkEntity.getArticleEntity().getCoverImgUrl(),
                bookmarkEntity.getArticleEntity().getTravelCompanion().getName(),
                travelStyleNames,
                bookmarkEntity.getArticleEntity().getNickname(),
                bookmarkCount,
                isBookmarked
        );
    }
}
