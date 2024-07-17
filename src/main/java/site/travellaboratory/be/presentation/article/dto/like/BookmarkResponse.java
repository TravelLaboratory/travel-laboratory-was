package site.travellaboratory.be.presentation.article.dto.like;

import java.time.LocalDate;
import java.util.List;
import site.travellaboratory.be.infrastructure.domains.article.entity.ArticleLocationEntity;
import site.travellaboratory.be.domain.article.enums.TravelStyle;
import site.travellaboratory.be.infrastructure.domains.bookmark.entity.Bookmark;

public record BookmarkResponse(
        Long articleId,
        String title,
        List<ArticleLocationEntity> location,
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
        List<String> travelStyleNames = bookmark.getArticleEntity().getTravelStyles().stream()
                .map(TravelStyle::getName)
                .toList();

        return new BookmarkResponse(
                bookmark.getArticleEntity().getId(),
                bookmark.getArticleEntity().getTitle(),
                bookmark.getArticleEntity().getLocationEntities(),
                bookmark.getArticleEntity().getStartAt(),
                bookmark.getArticleEntity().getEndAt(),
                bookmark.getArticleEntity().getExpense(),
                bookmark.getArticleEntity().getUserEntity().getProfileImgUrl(),
                bookmark.getArticleEntity().getCoverImageUrl(),
                bookmark.getArticleEntity().getTravelCompanion().getName(),
                travelStyleNames,
                bookmark.getArticleEntity().getNickname(),
                bookmarkCount,
                isBookmarked
        );
    }
}
