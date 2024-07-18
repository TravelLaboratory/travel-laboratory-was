package site.travellaboratory.be.article.presentation.response.like;

import java.time.LocalDate;
import java.util.List;
import site.travellaboratory.be.article.infrastructure.persistence.entity.ArticleLocationEntity;
import site.travellaboratory.be.article.domain.enums.TravelStyle;
import site.travellaboratory.be.article.infrastructure.persistence.entity.Bookmark;

public record BookmarkResponse(
        Long articleId,
        String title,
        List<ArticleLocationEntity> location,
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
                bookmark.getArticleEntity().getCoverImgUrl(),
                bookmark.getArticleEntity().getTravelCompanion().getName(),
                travelStyleNames,
                bookmark.getArticleEntity().getNickname(),
                bookmarkCount,
                isBookmarked
        );
    }
}
