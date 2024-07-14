package site.travellaboratory.be.presentation.article.dto.like;

import java.time.LocalDate;
import java.util.List;
import site.travellaboratory.be.infrastructure.domains.article.entity.ArticleLocationJpaEntity;
import site.travellaboratory.be.domain.article.enums.TravelStyle;
import site.travellaboratory.be.infrastructure.domains.bookmark.entity.Bookmark;

public record BookmarkResponse(
        Long articleId,
        String title,
        List<ArticleLocationJpaEntity> locationJpaEntities,
        LocalDate startAt,
        LocalDate endAt,
        String expense,
        String profileImageUrl,
        String coverImage,
        String travelCompanion,
        List<String> travelStyles,
        String name,
        Long bookmarkCount,
        Boolean isBookmarked
) {

    public static BookmarkResponse of(final Bookmark bookmark, final Long bookmarkCount, final Boolean isBookmarked) {
        List<String> travelStyleNames = bookmark.getArticleJpaEntity().getTravelStyles().stream()
                .map(TravelStyle::getName)
                .toList();

        return new BookmarkResponse(
                bookmark.getArticleJpaEntity().getId(),
                bookmark.getArticleJpaEntity().getTitle(),
                bookmark.getArticleJpaEntity().getLocationJpaEntities(),
                bookmark.getArticleJpaEntity().getStartAt(),
                bookmark.getArticleJpaEntity().getEndAt(),
                bookmark.getArticleJpaEntity().getExpense(),
                bookmark.getArticleJpaEntity().getUserJpaEntity().getProfileImgUrl(),
                bookmark.getArticleJpaEntity().getCoverImageUrl(),
                bookmark.getArticleJpaEntity().getTravelCompanion().getName(),
                travelStyleNames,
                bookmark.getArticleJpaEntity().getNickname(),
                bookmarkCount,
                isBookmarked
        );
    }
}
